package com.example.reserva_service.Service;

import com.example.reserva_service.Model.Usuario;
import com.example.reserva_service.Entity.ReservaEntity;
import com.example.reserva_service.Repository.ReservaRepository;
import com.lowagie.text.pdf.PdfWriter;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Document;
import org.springframework.web.client.RestTemplate;


@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JavaMailSender mailSender;

    public List<ReservaEntity> getReservasEntreFechas(LocalDateTime startDate, LocalDateTime endDate) {
        return reservaRepository.findByFechaReservaBetween(startDate, endDate);
    }

    public String obtenerNombreUsuario(String rut) {
        String url = "http://usuario-service/usuario/rut/" + rut;
        try {
            // Realizar la llamada al servicio de usuario
            Usuario usuario = restTemplate.getForObject(url, Usuario.class);
            if (usuario != null) {
                return usuario.getNombre();
            } else {
                return "Usuario no encontrado";
            }
        } catch (Exception e) {
            return "Error al obtener el nombre del usuario: " + e.getMessage();
        }
    }

    public Integer obtenerDescuentoGrupo(int cantPersonas) {
        ResponseEntity<Integer> desc = restTemplate.exchange(
                "http://descuentoGrupo-service/descuentoGrupo/calcular/" + cantPersonas,
                HttpMethod.GET, null, Integer.class);
        return desc.getBody();
    }

    public Map<Usuario, Integer> obtenerDescuentoFrecuencia(String rutUsuario, String rutUsuarios, LocalDateTime fechaReserva) {
        Map<Usuario, Integer> descuentos = new java.util.HashMap<>();

        List<String> allRuts = new ArrayList<>();
        allRuts.add(rutUsuario);
        List<String> extraRuts = Arrays.stream(rutUsuarios.split(","))
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .collect(Collectors.toList());
        allRuts.addAll(extraRuts);

        ResponseEntity<List<Usuario>> participantes = restTemplate.exchange("http://usuario-service/usuario/ruts/" + String.join(",", allRuts),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Usuario>>() {});
        List<Usuario> usuarios = participantes.getBody();

        for (Usuario usuario : usuarios) {
             Integer desc = restTemplate.getForObject("http://descuentoVisitas-service/descuentoVisitas/calcular/" + usuario.getRut() + "/" + fechaReserva, Integer.class);

             descuentos.put(usuario, desc);
        }
        return descuentos;
    }

    public byte[] generatePDF(ReservaEntity reservation, List<List<Object>> detail) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        // Abrir el documento
        document.open();

        // Agregar contenido al documento
        com.lowagie.text.Font font = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);
        document.add(new Paragraph("Comprobante de Reserva - KartingRM", font));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Código de reserva: RES-" + reservation.getId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormateada = reservation.getFechaReserva().format(formatter);
        document.add(new Paragraph("Fecha y hora: " + fechaFormateada));
        document.add(new Paragraph("N° de vueltas o Tiempo máximo: " + reservation.getVueltasTiempo()));
        document.add(new Paragraph("Cantidad de personas: " + reservation.getCantPersonas()));
        document.add(new Paragraph("Persona que hizo la reservación: " + obtenerNombreUsuario(reservation.getRutUsuario())));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        Stream.of("Nombre", "Tarifa base", "Desc. Grupo", "Desc. Frec.", "Desc. Cumple.", "Desc. Especial",
                        "Desc. Aplicado", "Subtotal", "IVA (19%)", "Total")
                .forEach(h -> {
                    PdfPCell cell = new PdfPCell(new Phrase(h));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });

        // Agregar los datos de los participantes
        for (List<Object> fila : detail) {
            for (Object col : fila) {
                table.addCell(String.valueOf(col));
            }
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public void sendVoucherByEmail(List<String> emails, byte[] pdf) {
        for (String email : emails) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email);
                helper.setSubject("Comprobante de Reserva KartingRM");
                helper.setText("Estimado cliente, adjuntamos el comprobante de su reserva en formato PDF. Preséntelo el día de su visita.");
                helper.addAttachment("comprobante_reserva.pdf", new ByteArrayResource(pdf));
                mailSender.send(message);
            } catch (MessagingException e) {
                // Manejo de error por cada destinatario individual
                System.err.println("Error al enviar correo a " + email);
                e.printStackTrace(); // Puedes reemplazar por log
            }
        }
    }

    private boolean isWithinWorkingHours(LocalDate date, LocalTime startTime, LocalTime endTime) {
        DayOfWeek day = date.getDayOfWeek();

        LocalTime apertura;
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY /*|| isHoliday(date)*/) {
            apertura = LocalTime.of(10, 0); // Fin de semana o feriado
        } else {
            apertura = LocalTime.of(14, 0); // Lunes a Viernes
        }
        LocalTime cierre = LocalTime.of(22, 0);

        return !startTime.isBefore(apertura) && !endTime.isAfter(cierre);
    }
}
