package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    boolean existsByPatrocinio_IdAndCategoria(Long idPatrocinio, CategoriaDocumento categoria);

    List<Documento> findByPatrocinio_IdOrderByDataHoraCriacaoDesc(Long idPatrocinio);

}
