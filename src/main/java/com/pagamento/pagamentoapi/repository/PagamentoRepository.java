package com.pagamento.pagamentoapi.repository;

import com.pagamento.pagamentoapi.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByCodigoDebito(Integer codigoDebito);
    List<Pagamento> findByCpfCnpjPagador(String cpfCnpjPagador);
    List<Pagamento> findByStatus(String status);
}
