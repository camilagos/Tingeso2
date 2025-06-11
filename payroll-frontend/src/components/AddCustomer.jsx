import { useState } from "react";
import { useNavigate } from "react-router-dom";
import usuarioService from "../services/usuario.service";

import {
  Box,
  TextField,
  Button,
  Typography
} from "@mui/material";
import SaveIcon from "@mui/icons-material/Save";

const AddUser = () => {
  const [user, setUser] = useState({
    nombre: "",
    correo: "",
    rut: "",
    contrasena: "",
    cumpleanos: "",
    admin: true,
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setUser((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    try {
      const res = await usuarioService.save(user);
  
      if (res.status === 200) {
        localStorage.setItem("preLoginEmail", user.correo);
        localStorage.setItem("preLoginPassword", user.contrasena);
        localStorage.setItem("preLoginRut", user.rut);
        alert("Usuario registrado con éxito");
        navigate("/login");
      } else {
        alert("No se pudo registrar el usuario.");
      }
    } catch (err) {
      console.error(err);
      alert("Error al registrar usuario");
    }
  };
  

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", mt: 5 }}>
      <Typography variant="h5" gutterBottom>
        Registrar Usuario
      </Typography>
      <form onSubmit={handleSubmit}>
        <TextField fullWidth margin="normal" name="nombre" label="Nombre" onChange={handleChange} required />
        <TextField fullWidth margin="normal" name="correo" label="Email" onChange={handleChange} required />
        <TextField fullWidth margin="normal" name="rut" label="RUT" onChange={handleChange} required />
        <TextField fullWidth margin="normal" name="contrasena" type="password" label="Contraseña" onChange={handleChange} required />
        <TextField fullWidth margin="normal" name="cumpleanos" type="date" InputLabelProps={{ shrink: true }} label="Fecha de nacimiento" onChange={handleChange} required />
        <Button type="submit" variant="contained" color="primary" startIcon={<SaveIcon />}>
          Registrar
        </Button>
      </form>
    </Box>
  );
};

export default AddUser;
