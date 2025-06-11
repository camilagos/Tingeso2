import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {Box, Typography, Paper, Divider, Button} from "@mui/material";
import usuarioService from "../services/usuario.service";

const ProfileCustomer = () => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (!userData) {
      alert("Debes iniciar sesión para ver tu perfil");
      navigate("/login");
    } else {
      setUser(JSON.parse(userData));
    }
  }, [navigate]);

  const handleDeleteAccount = async () => {
    if (!user) return;
    const confirmDelete = window.confirm("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.");
    if (confirmDelete) {
      try {
        await usuarioService.remove(user.id);
        localStorage.removeItem("user");
        alert("Tu cuenta ha sido eliminada.");
        window.location.href = "/";
      } catch (error) {
        console.error(error);
        alert("Hubo un error al eliminar tu cuenta.");
      }
    }
  };

  if (!user) return null;

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", mt: 5 }}>
      <Paper elevation={3} sx={{ p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Perfil del Usuario
        </Typography>
        <Divider sx={{ mb: 2 }} />

        <Typography><strong>Nombre:</strong> {user.nombre}</Typography>
        <Typography><strong>Email:</strong> {user.correo}</Typography>
        <Typography><strong>RUT:</strong> {user.rut}</Typography>
        <Typography><strong>Fecha de nacimiento:</strong> {user.cumpleanos}</Typography>

        <Divider sx={{ my: 2 }} />
        
        <Button variant="contained" color="error" onClick={handleDeleteAccount} fullWidth>
          Eliminar Cuenta
        </Button>

      </Paper>
    </Box>
  );
};

export default ProfileCustomer;
