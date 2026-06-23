package com.app.confeitaria.docelivery.model.repository;

import com.app.confeitaria.docelivery.model.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioIdAndTipo(Long usuarioId, String tipo);

    Optional<Favorito> findByUsuarioIdAndTipoAndReferenciaId(
            Long usuarioId,
            String tipo,
            Long referenciaId);

    boolean existsByUsuarioIdAndTipoAndReferenciaId(
            Long usuarioId,
            String tipo,
            Long referenciaId);

    List<Favorito> findByUsuarioId(Long usuarioId);

    long countByUsuarioIdAndTipo(Long usuarioId, String tipo);

    void deleteByUsuarioIdAndTipoAndReferenciaId(
            Long usuarioId,
            String tipo,
            Long referenciaId);

    @Query("SELECT f.referenciaId FROM Favorito f " +
           "WHERE f.usuario.id = :usuarioId AND f.tipo = :tipo " +
           "ORDER BY f.dataCriacao DESC")
    List<Long> findReferenciasIdsByUsuarioAndTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") String tipo);
}
