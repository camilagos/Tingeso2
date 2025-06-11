import { useEffect, useState } from "react";
import {
  Box, Typography, TextField, Button,
  Table, TableHead, TableRow, TableCell, TableBody,
  IconButton, Paper, Divider
} from "@mui/material";
import { Edit, Delete } from "@mui/icons-material";
import descuentoGrupoService from "../services/descGrupo.service";
import descuentoVisitasService from "../services/descVisitas.service";

const Descuentos = () => {
  const [grupoDescuentos, setGrupoDescuentos] = useState([]);
  const [visitaDescuentos, setVisitaDescuentos] = useState([]);

  const [grupoForm, setGrupoForm] = useState({ minPersonas: "", maxPersonas: "", descuento: "" });
  const [visitaForm, setVisitaForm] = useState({ categoria: "", minVisitas: "", maxVisitas: "", descuento: "" });

  const [editGrupoId, setEditGrupoId] = useState(null);
  const [editVisitaId, setEditVisitaId] = useState(null);

  const fetchData = async () => {
    const [grupoRes, visitaRes] = await Promise.all([
      descuentoGrupoService.getAll(),
      descuentoVisitasService.getAll()
    ]);
    setGrupoDescuentos(grupoRes.data || []);
    setVisitaDescuentos(visitaRes.data || []);
  };

  useEffect(() => { fetchData(); }, []);

  const handleGrupoSubmit = async (e) => {
    e.preventDefault();
    const action = editGrupoId ? descuentoGrupoService.update(editGrupoId, grupoForm) : descuentoGrupoService.save(grupoForm);
    await action;
    resetGrupoForm();
    fetchData();
  };

  const handleVisitaSubmit = async (e) => {
    e.preventDefault();
    const action = editVisitaId ? descuentoVisitasService.update(editVisitaId, visitaForm) : descuentoVisitasService.save(visitaForm);
    await action;
    resetVisitaForm();
    fetchData();
  };

  const resetGrupoForm = () => {
    setGrupoForm({ minPersonas: "", maxPersonas: "", descuento: "" });
    setEditGrupoId(null);
  };

  const resetVisitaForm = () => {
    setVisitaForm({ categoria: "", minVisitas: "", maxVisitas: "", descuento: "" });
    setEditVisitaId(null);
  };

  const handleGrupoEdit = (d) => {
    setGrupoForm({ minPersonas: d.minPersonas, maxPersonas: d.maxPersonas, descuento: d.descuento });
    setEditGrupoId(d.id);
  };

  const handleVisitaEdit = (d) => {
    setVisitaForm({ categoria: d.categoria, minVisitas: d.minVisitas, maxVisitas: d.maxVisitas, descuento: d.descuento });
    setEditVisitaId(d.id);
  };

  const handleGrupoDelete = async (id) => {
    const confirmar = window.confirm("¿Estás seguro que deseas eliminar este descuento por grupo?");
    if (!confirmar) return;

    try {
        await descuentoGrupoService.remove(id);
        fetchData();
    } catch (err) {
        console.error("Error al eliminar descuento por grupo:", err);
    }
    };

const handleVisitaDelete = async (id) => {
  const confirmar = window.confirm("¿Estás seguro que deseas eliminar este descuento por visitas?");
  if (!confirmar) return;

  try {
    await descuentoVisitasService.remove(id);
    fetchData();
  } catch (err) {
    console.error("Error al eliminar descuento por visitas:", err);
  }
};


  return (
    <Box sx={{ maxWidth: 900, mx: "auto", mt: 4 }}>
      <Typography variant="h5" gutterBottom>Gestión de Descuentos</Typography>

      {/* --- GRUPO --- */}
      <Paper sx={{ p: 2, mt: 2, mb: 4 }}>
        <Typography variant="h6">Descuentos por Grupo</Typography>
        <form onSubmit={handleGrupoSubmit}>
          <TextField label="Mín. Personas" name="minPersonas" type="number" value={grupoForm.minPersonas} onChange={e => setGrupoForm({ ...grupoForm, minPersonas: e.target.value })} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Máx. Personas" name="maxPersonas" type="number" value={grupoForm.maxPersonas} onChange={e => setGrupoForm({ ...grupoForm, maxPersonas: e.target.value })} sx={{ mr: 2, mt: 2 }} />
          <TextField label="% Descuento" name="descuento" type="number" value={grupoForm.descuento} onChange={e => setGrupoForm({ ...grupoForm, descuento: e.target.value })} sx={{ mt: 2 }} />
          <Box><Button type="submit" sx={{ mt: 2 }}>{editGrupoId ? "Actualizar" : "Crear"}</Button></Box>
        </form>

        <Table size="small" sx={{ mt: 3 }}>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Mín.</TableCell>
              <TableCell>Máx.</TableCell>
              <TableCell>Descuento</TableCell>
              <TableCell>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {grupoDescuentos.map(d => (
              <TableRow key={d.id}>
                <TableCell>{d.id}</TableCell>
                <TableCell>{d.minPersonas}</TableCell>
                <TableCell>{d.maxPersonas}</TableCell>
                <TableCell>{d.descuento}%</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleGrupoEdit(d)}><Edit /></IconButton>
                  <IconButton onClick={() => handleGrupoDelete(d.id)} color="error"><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>

      <Divider />

      {/* --- VISITAS --- */}
      <Paper sx={{ p: 2, mt: 4 }}>
        <Typography variant="h6">Descuentos por Visitas</Typography>
        <form onSubmit={handleVisitaSubmit}>
          <TextField label="Categoría" name="categoria" value={visitaForm.categoria} onChange={e => setVisitaForm({ ...visitaForm, categoria: e.target.value })} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Mín. Visitas" name="minVisitas" type="number" value={visitaForm.minVisitas} onChange={e => setVisitaForm({ ...visitaForm, minVisitas: e.target.value })} sx={{ mr: 2, mt: 2 }} />
          <TextField label="Máx. Visitas" name="maxVisitas" type="number" value={visitaForm.maxVisitas} onChange={e => setVisitaForm({ ...visitaForm, maxVisitas: e.target.value })} sx={{ mr: 2, mt: 2 }} />
          <TextField label="% Descuento" name="descuento" type="number" value={visitaForm.descuento} onChange={e => setVisitaForm({ ...visitaForm, descuento: e.target.value })} sx={{ mt: 2 }} />
          <Box><Button type="submit" sx={{ mt: 2 }}>{editVisitaId ? "Actualizar" : "Crear"}</Button></Box>
        </form>

        <Table size="small" sx={{ mt: 3 }}>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Categoría</TableCell>
              <TableCell>Mín.</TableCell>
              <TableCell>Máx.</TableCell>
              <TableCell>Descuento</TableCell>
              <TableCell>Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {visitaDescuentos.map(d => (
              <TableRow key={d.id}>
                <TableCell>{d.id}</TableCell>
                <TableCell>{d.categoria}</TableCell>
                <TableCell>{d.minVisitas}</TableCell>
                <TableCell>{d.maxVisitas}</TableCell>
                <TableCell>{d.descuento}%</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleVisitaEdit(d)}><Edit /></IconButton>
                  <IconButton onClick={() => handleVisitaDelete(d.id)} color="error"><Delete /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </Box>
  );
};

export default Descuentos;
