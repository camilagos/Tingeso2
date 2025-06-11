import { useEffect, useState } from "react";
import {
  Box, Typography, TextField, Button,
  Table, TableHead, TableRow, TableCell, TableBody,
  IconButton, Paper, Divider
} from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";
import tarifaService from "../services/tarifa.service";
import tarifaEspecialService from "../services/tarifaEspecial.service";

const Tarifa = () => {
  const [tarifas, setTarifas] = useState([]);
  const [tarifasEspeciales, setTarifasEspeciales] = useState([]);

  const [form, setForm] = useState({
    tiempoVueltas: "",
    precio: "",
    duracionReserva: "",
  });

  const [especialForm, setEspecialForm] = useState({
    fecha: "",
    porcentajeTarifa: "",
    descripcion: "",
    esRecargo: true,
  });

  const [editingId, setEditingId] = useState(null);
  const [editEspecialId, setEditEspecialId] = useState(null);

  const fetchTarifas = async () => {
    try {
      const res = await tarifaService.getAll();
      setTarifas(res.data || []);
    } catch (err) {
      console.error("Error al cargar tarifas:", err);
    }
  };

  const fetchTarifasEspeciales = async () => {
    try {
      const res = await tarifaEspecialService.getAll();
      setTarifasEspeciales(res.data || []);
    } catch (err) {
      console.error("Error al cargar tarifas especiales:", err);
    }
  };

  useEffect(() => {
    fetchTarifas();
    fetchTarifasEspeciales();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleEspecialChange = (e) => {
    const { name, value } = e.target;
    setEspecialForm((prev) => ({ ...prev, [name]: name === "esRecargo" ? value === "true" : value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId !== null) {
        await tarifaService.update(editingId, form);
        alert("Tarifa actualizada correctamente");
      } else {
        await tarifaService.save(form);
        alert("Tarifa creada correctamente");
      }
      setForm({ tiempoVueltas: "", precio: "", duracionReserva: "" });
      setEditingId(null);
      fetchTarifas();
    } catch (err) {
      console.error("Error al guardar tarifa:", err);
    }
  };

  const handleEspecialSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editEspecialId !== null) {
        await tarifaEspecialService.update(editEspecialId, especialForm);
        alert("Tarifa especial actualizada correctamente");
      } else {
        await tarifaEspecialService.save(especialForm);
        alert("Tarifa especial creada correctamente");
      }
      setEspecialForm({ fecha: "", porcentajeTarifa: "", descripcion: "", esRecargo: true });
      setEditEspecialId(null);
      fetchTarifasEspeciales();
    } catch (err) {
      console.error("Error al guardar tarifa especial:", err);
    }
  };

  const handleEdit = (tarifa) => {
    setForm({
      tiempoVueltas: tarifa.tiempoVueltas,
      precio: tarifa.precio,
      duracionReserva: tarifa.duracionReserva,
    });
    setEditingId(tarifa.id);
  };

  const handleEspecialEdit = (tarifa) => {
    setEspecialForm(tarifa);
    setEditEspecialId(tarifa.id);
  };

  const handleDelete = async (id) => {
  const confirmar = window.confirm("¿Estás seguro que deseas eliminar esta tarifa?");
  if (!confirmar) return;

  try {
    await tarifaService.remove(id);
    fetchTarifas();
  } catch (err) {
    console.error("Error al eliminar tarifa:", err);
  }
};

const handleEspecialDelete = async (id) => {
  const confirmar = window.confirm("¿Estás seguro que deseas eliminar esta tarifa especial?");
  if (!confirmar) return;

  try {
    await tarifaEspecialService.remove(id);
    fetchTarifasEspeciales();
  } catch (err) {
    console.error("Error al eliminar tarifa especial:", err);
  }
};


  return (
    <Box sx={{ maxWidth: 900, mx: "auto", mt: 4 }}>
      <Typography variant="h5" gutterBottom>Gestión de Tarifas</Typography>

      {/* --- TARIFAS --- */}
      <Paper sx={{ p: 2, mt: 2, mb: 4 }}>
        <Typography variant="h6">Tarifas Normales</Typography>
        <form onSubmit={handleSubmit}>
          <TextField label="Vueltas o Minutos" name="tiempoVueltas" type="number" value={form.tiempoVueltas} onChange={handleChange} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Precio ($)" name="precio" type="number" value={form.precio} onChange={handleChange} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Duración Total (min)" name="duracionReserva" type="number" value={form.duracionReserva} onChange={handleChange} sx={{ mt: 2 }} />
          <Box><Button type="submit" sx={{ mt: 2 }}>{editingId ? "Actualizar" : "Crear"}</Button></Box>
        </form>

        <Table size="small" sx={{ mt: 3 }}>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Vueltas/Minutos</TableCell>
              <TableCell>Precio</TableCell>
              <TableCell>Duración</TableCell>
              <TableCell>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tarifas.map((t) => (
              <TableRow key={t.id}>
                <TableCell>{t.id}</TableCell>
                <TableCell>{t.tiempoVueltas}</TableCell>
                <TableCell>${t.precio.toLocaleString()}</TableCell>
                <TableCell>{t.duracionReserva} min</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleEdit(t)}><Edit /></IconButton>
                  <IconButton onClick={() => handleDelete(t.id)} color="error"><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>

      <Divider />

      {/* --- ESPECIALES --- */}
      <Paper sx={{ p: 2, mt: 4 }}>
        <Typography variant="h6">Tarifas Especiales</Typography>
        <form onSubmit={handleEspecialSubmit}>
          <TextField label="Fecha" name="fecha" type="date" value={especialForm.fecha} onChange={handleEspecialChange} sx={{ mr: 2, mt: 2 }} InputLabelProps={{ shrink: true }} />
          <TextField label="% Tarifa" name="porcentajeTarifa" type="number" value={especialForm.porcentajeTarifa} onChange={handleEspecialChange} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Descripción" name="descripcion" value={especialForm.descripcion} onChange={handleEspecialChange} sx={{ mr: 2, mt: 2 }} />
          <TextField label="¿Es recargo?" name="esRecargo" select value={especialForm.esRecargo} onChange={handleEspecialChange} SelectProps={{ native: true }} sx={{ mt: 2 }}>
            <option value="true">Sí</option>
            <option value="false">No</option>
          </TextField>
          <Box><Button type="submit" sx={{ mt: 2 }}>{editEspecialId ? "Actualizar" : "Crear"}</Button></Box>
        </form>

        <Table size="small" sx={{ mt: 3 }}>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Fecha</TableCell>
              <TableCell>%</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Descripción</TableCell>
              <TableCell>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tarifasEspeciales.map((te) => (
              <TableRow key={te.id}>
                <TableCell>{te.id}</TableCell>
                <TableCell>{te.fecha}</TableCell>
                <TableCell>{te.porcentajeTarifa}%</TableCell>
                <TableCell>{te.esRecargo ? "Recargo" : "Descuento"}</TableCell>
                <TableCell>{te.descripcion}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleEspecialEdit(te)}><Edit /></IconButton>
                  <IconButton onClick={() => handleEspecialDelete(te.id)} color="error"><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </Box>
  );
};

export default Tarifa;
