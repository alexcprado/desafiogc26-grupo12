package br.com.planeta_atlantida.rbs.api.repository;

import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PesquisaAtivacaoRepository extends JpaRepository<PesquisaAtivacao, Long> {

    Page<PesquisaAtivacao> findAll(Pageable pageable);

    boolean existsByPatrocinio_Id(Long idPatrocinio);

    Optional<PesquisaAtivacao> findByPatrocinio_Id(Long idPatrocinio);

}
