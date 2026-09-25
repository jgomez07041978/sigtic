package mx.gob.tabasco.cmaig.sigtic.sai.service;

import mx.gob.tabasco.cmaig.sigtic.sai.model.CatTipoCorreoElectronico;
import mx.gob.tabasco.cmaig.sigtic.sai.repository.CatTipoCorreoRepository;
import mx.gob.tabasco.cmaig.sigtic.sas.security.UserDetailsSesion;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CatTipoCorreoServiceImp implements CatTipoCorreoService {

    private final CatTipoCorreoRepository catTipoCorreoRepository;

    public CatTipoCorreoServiceImp(CatTipoCorreoRepository catTipoCorreoRepository) {
        this.catTipoCorreoRepository = catTipoCorreoRepository;
    }


    @Override
    public List<CatTipoCorreoElectronico> getListaTipoCorreo() {
        return catTipoCorreoRepository.findAllByCveEstatusOrderByNumeroOrdenAsc("VIG");
    }


    @Override
    public CatTipoCorreoElectronico getCatTipoCorreoElectronicoCve(String cveTipoCorreoElectronico) {
        return catTipoCorreoRepository.findById(cveTipoCorreoElectronico)
                .orElseThrow(() -> new NoSuchElementException("El tipo de correo no existe"));
    }


    @Override
    public CatTipoCorreoElectronico guardarTipoCorreo(CatTipoCorreoElectronico dto) {
        UserDetailsSesion authUser = (UserDetailsSesion)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (dto.getOpcion().equals("Nuevo")) {

            // Validar que la clave no exista ya
            Optional<CatTipoCorreoElectronico> existente =
                    catTipoCorreoRepository.findById(dto.getCveTipoCorreoElectronico());
            if (existente.isPresent())
                throw new NoSuchElementException("El tipo de correo electrónico ya existe.");

            CatTipoCorreoElectronico entity = new CatTipoCorreoElectronico();
            entity.setCveTipoCorreoElectronico(dto.getCveTipoCorreoElectronico());
            entity.setNombreTipoCorreoElectronico(dto.getNombreTipoCorreoElectronico());
//            entity.setNombreTipoCorreoElectronicoA(dto.getNombreTipoCorreoElectronicoA());
//            entity.setAlcanceTipoCorreoElectronico(dto.getAlcanceTipoCorreoElectronico());
            entity.setNumeroOrden(dto.getNumeroOrden());
            entity.setCveEstatus(dto.getCveEstatus());
            entity.setUsuarioMtto(authUser.getUsername());
            entity.setFechaRegistro(new Timestamp(new Date().getTime()));
            entity.setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            entity.setIdTipoCorreo(catTipoCorreoRepository.getNextIdTipoCorreo());
            entity.setIdSubtipoServicio(dto.getIdSubtipoServicio());

            return catTipoCorreoRepository.save(entity);

        } else {

            Optional<CatTipoCorreoElectronico> op =
                    catTipoCorreoRepository.findById(dto.getCveTipoCorreoElectronico());
            if (op.isPresent()) {
                op.get().setNombreTipoCorreoElectronico(dto.getNombreTipoCorreoElectronico());
//                op.get().setNombreTipoCorreoElectronicoA(dto.getNombreTipoCorreoElectronicoA());
//                op.get().setAlcanceTipoCorreoElectronico(dto.getAlcanceTipoCorreoElectronico());
                op.get().setNumeroOrden(dto.getNumeroOrden());
                op.get().setCveEstatus(dto.getCveEstatus());
                op.get().setUsuarioMtto(authUser.getUsername());
                op.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
                op.get().setIdSubtipoServicio(dto.getIdSubtipoServicio());
                return catTipoCorreoRepository.save(op.get());
            } else {
                throw new NoSuchElementException("El tipo de correo electrónico no existe");
            }
        }
    }


    @Override
    public CatTipoCorreoElectronico deleteTipoCorreoElectronico(CatTipoCorreoElectronico dto) {
        UserDetailsSesion authUser = (UserDetailsSesion)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CatTipoCorreoElectronico> op =
                catTipoCorreoRepository.findById(dto.getCveTipoCorreoElectronico());

        if (op.isPresent()) {
            op.get().setCveEstatus("NOV");
            op.get().setUsuarioMtto(authUser.getUsername());
            op.get().setFechaUltimoCambio(new Timestamp(new Date().getTime()));
            return catTipoCorreoRepository.save(op.get());
        } else {
            throw new NoSuchElementException("El tipo de correo electrónico no existe");
        }
    }

}