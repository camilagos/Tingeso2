package com.example.descuentoGrupo_service.Service;

import com.example.descuentoGrupo_service.Entity.DescuentoGrupoEntity;
import com.example.descuentoGrupo_service.Repository.DescuentoGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DescuentoGrupoService {

    @Autowired
    DescuentoGrupoRepository descuentoGrupoRepository;

    public DescuentoGrupoEntity saveDescuentoGrupo(DescuentoGrupoEntity descuentoGrupo) {
        return descuentoGrupoRepository.save(descuentoGrupo);
    }

    public List<DescuentoGrupoEntity> getAllDescuentosGrupo() {
        return descuentoGrupoRepository.findAll();
    }

    public DescuentoGrupoEntity updateDescuentoGrupo(DescuentoGrupoEntity descuentoGrupo) {
        return descuentoGrupoRepository.save(descuentoGrupo);
    }

    public boolean deleteDescuentoGrupo(int id) throws Exception {
        try {
            descuentoGrupoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public DescuentoGrupoEntity buscarDescuentoGrupo(int cantPersonas) {
        DescuentoGrupoEntity descuentoGrupo = descuentoGrupoRepository.findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(cantPersonas, cantPersonas);
        if (descuentoGrupo != null) {
            return descuentoGrupo;
        } else {
            return null;
        }
    }
}
