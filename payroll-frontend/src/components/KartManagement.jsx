import { useEffect, useState } from "react";
import {
  Box,
  Typography,
  Button,
  Paper,
  Divider,
  TextField
} from "@mui/material";
import kartService from "../services/kart.service";

const KartManagement = () => {
  const [availableKarts, setAvailableKarts] = useState([]);
  const [unavailableKarts, setUnavailableKarts] = useState([]);
  const [newKartCode, setNewKartCode] = useState("");
  const [newKartModel, setNewKartModel] = useState("Sodikart RT8");

  const loadKarts = async () => {
    try {
      const available = await kartService.getByDisponibilidad(true);
      const unavailable = await kartService.getByDisponibilidad(false);
      setAvailableKarts(available.data);
      setUnavailableKarts(unavailable.data);
    } catch (error) {
      console.error("Error cargando karts", error);
    }
  };

  useEffect(() => {
    loadKarts();
  }, []);

  const toggleAvailability = async (kart) => {
    try {
      const updatedKart = { ...kart, disponible: !kart.disponible };
      await kartService.update(updatedKart);
      loadKarts();
    } catch (error) {
      console.error("Error cambiando disponibilidad", error);
    }
  };

  const handleAddKart = async () => {
    if (!newKartCode.trim()) return;

    try {
      await kartService.save({
        codigo: newKartCode.trim(),
        modelo: newKartModel.trim() || "Sodikart RT8",
        disponible: true
      });
      alert("Kart agregado correctamente");
      setNewKartCode("");
      setNewKartModel("Sodikart RT8");
      loadKarts();
    } catch (error) {
      console.error("Error agregando kart:", error);
      alert("No se pudo agregar el kart. Verifica si el código ya existe.");
    }
  };

  const handleDeleteKart = async (id) => {
    if (!window.confirm("¿Seguro que deseas eliminar este kart?")) return;
    try {
      await kartService.remove(id);
      loadKarts();
    } catch (error) {
      console.error("Error eliminando kart", error);
      alert("No se pudo eliminar el kart.");
    }
  };

  return (
    <Box sx={{ maxWidth: 800, margin: "auto", mt: 5 }}>
      <Paper elevation={3} sx={{ p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Gestión de Karts
        </Typography>
        <Divider sx={{ mb: 3 }} />

        <Box sx={{ mb: 4 }}>
          <Typography variant="h6">Agregar Nuevo Kart</Typography>
          <Box sx={{ display: "flex", gap: 2, mt: 1, flexWrap: "wrap" }}>
            <TextField
              label="Código del Kart"
              value={newKartCode}
              onChange={(e) => setNewKartCode(e.target.value)}
            />
            <TextField
              label="Modelo"
              value={newKartModel}
              onChange={(e) => setNewKartModel(e.target.value)}
            />
            <Button
              variant="contained"
              onClick={handleAddKart}
              disabled={!newKartCode.trim()}
            >
              Agregar
            </Button>
          </Box>
        </Box>

        <Box sx={{ mb: 4 }}>
          <Typography variant="h6">
            Karts Disponibles ({availableKarts.length})
          </Typography>
          <Divider sx={{ my: 1 }} />
          {availableKarts.length === 0 ? (
            <Typography>No hay karts disponibles.</Typography>
          ) : (
            availableKarts.map((kart) => (
              <Box
                key={kart.id}
                sx={{ mb: 2, display: "flex", justifyContent: "space-between", alignItems: "center" }}
              >
                <Typography>
                  <strong>Código:</strong> {kart.codigo} — <strong>Modelo:</strong> {kart.modelo || "Desconocido"}
                </Typography>
                <Box sx={{ display: "flex", gap: 1 }}>
                  <Button
                    variant="contained"
                    color="warning"
                    onClick={() => toggleAvailability(kart)}
                  >
                    Marcar como No Disponible
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleDeleteKart(kart.id)}
                  >
                    Eliminar
                  </Button>
                </Box>
              </Box>
            ))
          )}
        </Box>

        <Box>
          <Typography variant="h6">
            Karts No Disponibles ({unavailableKarts.length})
          </Typography>
          <Divider sx={{ my: 1 }} />
          {unavailableKarts.length === 0 ? (
            <Typography>No hay karts no disponibles.</Typography>
          ) : (
            unavailableKarts.map((kart) => (
              <Box
                key={kart.id}
                sx={{ mb: 2, display: "flex", justifyContent: "space-between", alignItems: "center" }}
              >
                <Typography>
                  <strong>Código:</strong> {kart.codigo} — <strong>Modelo:</strong> {kart.modelo || "Desconocido"}
                </Typography>
                <Box sx={{ display: "flex", gap: 1 }}>
                  <Button
                    variant="contained"
                    color="success"
                    onClick={() => toggleAvailability(kart)}
                  >
                    Marcar como Disponible
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleDeleteKart(kart.id)}
                  >
                    Eliminar
                  </Button>
                </Box>
              </Box>
            ))
          )}
        </Box>
      </Paper>
    </Box>
  );
};

export default KartManagement;
