package com.example.projeto.controller;

import javax.validation.Valid; // Atenção: Mude para javax

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projeto.model.Pessoa;
import com.example.projeto.service.PessoaService;

import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/pessoas")
public class PessoaWebController {

    private final PessoaService pessoaService;

    public PessoaWebController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    // 1. Unifica a página de listagem e cadastro
    @GetMapping("/gerenciar")
    public String exibirPaginaGerenciar(Model model) {
        // Envia a lista de pessoas existentes para a tabela
        model.addAttribute("lista", pessoaService.listarPessoas());
        // Envia um objeto Pessoa novo e vazio para o formulário
        model.addAttribute("pessoa", new Pessoa());
        return "pessoas/gerenciar"; // Vamos criar este novo arquivo HTML
    }

    // Redireciona a rota principal para a nova página
    @GetMapping
    public String index() {
        return "redirect:/pessoas/gerenciar";
    }


    // 2. O método de cadastrar agora redireciona para a página unificada
    @PostMapping("/cadastrar")
    public String cadastrarPessoa(
            @Valid @ModelAttribute("pessoa") Pessoa pessoa,
            BindingResult result,
            RedirectAttributes ra,
            Model model) { // Adicionamos o Model aqui

        if (result.hasErrors()) {
            // Se houver erros, precisamos reenviar a lista para a página não quebrar
            model.addAttribute("lista", pessoaService.listarPessoas());
            return "pessoas/gerenciar"; // Retorna para a mesma página, mostrando os erros
        }

        pessoaService.salvarPessoa(pessoa);
        ra.addFlashAttribute("success", "Pessoa cadastrada com sucesso!");
        return "redirect:/pessoas/gerenciar"; // Redireciona de volta para a página principal
    }

    // 3. O método de exclusão continua o mesmo, apenas redireciona para a nova página
    @PostMapping("/{id}/excluir")
    public String excluirPessoa(@PathVariable Long id, RedirectAttributes ra) {
        pessoaService.deletarPessoa(id);
        ra.addFlashAttribute("success", "Pessoa excluída com sucesso!");
        return "redirect:/pessoas/gerenciar";
    }

    // O método de detalhes não é mais necessário, pois a exclusão será direto na lista.
}