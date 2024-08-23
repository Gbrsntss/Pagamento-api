package com.pagamento.pagamentoapi.controller;

import com.pagamento.pagamentoapi.model.Pagamento;
import com.pagamento.pagamentoapi.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody Pagamento pagamento) throws Exception {
        Pagamento novoPagamento = pagamentoService.criarPagamento(pagamento);
        return ResponseEntity.ok(novoPagamento);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        try {
            novoStatus = novoStatus.replaceAll("^\"|\"$", "");

            Pagamento pagamentoAtualizado = pagamentoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(pagamentoAtualizado);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }


    @GetMapping
    public List<Pagamento> listarPagamentos(
            @RequestParam(required = false) Integer codigoDebito,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) String status) {
        return pagamentoService.listarPagamentos(codigoDebito, cpfCnpj, status);
    }

    @PutMapping("/{id}/excluir")
    public ResponseEntity<Object> excluirLogicamente(@PathVariable Long id) {
        try {
            Pagamento pagamentoAtualizado = pagamentoService.excluirLogicamente(id);
            return ResponseEntity.ok(pagamentoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, String>() {{
                put("erro", e.getMessage());
            }});
        }
    }
}
