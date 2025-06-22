import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import reservaService from "../services/reserva.service";
import tarifaService from "../services/tarifa.service";
import {
  Box,
  TextField,
  Button,
  Typography,
  MenuItem
} from "@mui/material";
import SaveIcon from "@mui/icons-material/Save";

const AddReservation = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const isAdmin = user?.admin || false;

  const [form, setForm] = useState({
    rutUsuario: !isAdmin ? user?.rut || "" : "",
    rutsUsuarios: "",
    fechaReserva: "",
    vueltasTiempo: "",
    cantPersonas: 1,
  });

  const [tarifas, setTarifas] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTarifas = async () => {
      try {
        const res = await tarifaService.getAll();
        setTarifas(res.data || []);
      } catch (err) {
        console.error("Error al cargar tarifas:", err);
        alert("No se pudieron cargar las opciones de tarifas");
      }
    };

    fetchTarifas();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "cantPersonas" || name === "vueltasTiempo" ? parseInt(value) : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await reservaService.save(form);
      alert("Reserva realizada con éxito");
      navigate("/");
    } catch (err) {
      console.error("Error al crear la reserva:", err);
      alert("No se pudo realizar la reserva. Verifica los datos ingresados o intenta con otro horario.");
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", mt: 5 }}>
      <Typography variant="h5" gutterBottom>
        Crear Nueva Reserva
      </Typography>
      <form onSubmit={handleSubmit}>
        <TextField
          fullWidth
          margin="normal"
          name="rutUsuario"
          label="RUT del Usuario Principal"
          value={form.rutUsuario}
          onChange={handleChange}
          required
        />
        <TextField
          fullWidth
          margin="normal"
          name="rutsUsuarios"
          label="RUTs Adicionales (separados por coma)"
          value={form.rutsUsuarios}
          onChange={handleChange}
          helperText="Los RUTs ingresados deben corresponder a usuarios registrados en el sistema."
        />
        <TextField
          fullWidth
          margin="normal"
          type="datetime-local"
          name="fechaReserva"
          label="Fecha y Hora"
          InputLabelProps={{ shrink: true }}
          value={form.fechaReserva}
          onChange={handleChange}
          required
          helperText="Las reservas deben realizarse dentro del horario de atención: lunes a viernes de 14:00 a 22:00 h, fines de semana y feriados de 10:00 a 22:00 h."
        />
        <TextField
          fullWidth
          select
          margin="normal"
          name="vueltasTiempo"
          label="Vueltas o Tiempo (min)"
          value={form.vueltasTiempo}
          onChange={handleChange}
          required
        >
          {tarifas.map((t) => (
            <MenuItem key={t.tiempoVueltas} value={t.tiempoVueltas}>
              {t.tiempoVueltas} vueltas o {t.tiempoVueltas} min - ${t.precio}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          fullWidth
          margin="normal"
          type="number"
          name="cantPersonas"
          label="Número de Personas"
          value={form.cantPersonas}
          onChange={handleChange}
          //inputProps={{ min: 1, max: 15 }}
          required
        />

        <Button
          type="submit"
          variant="contained"
          color="primary"
          startIcon={<SaveIcon />}
          sx={{ mt: 2 }}
        >
          Reservar
        </Button>
      </form>
    </Box>
  );
};

export default AddReservation;
