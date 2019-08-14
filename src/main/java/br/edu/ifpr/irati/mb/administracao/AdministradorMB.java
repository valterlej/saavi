/*
 * Copyright 2019 Valter Luís Estevam Junior
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 */
package br.edu.ifpr.irati.mb.administracao;

import br.edu.ifpr.irati.mb.util.CrudMB;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Administrador;
import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * Controlador da interface de gerenciamento de administradores.
 *
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class AdministradorMB extends CrudMB<Administrador> implements Serializable {

    /**
     * Nova senha registro em cadastro.
     * 
     * Não é mostrada em formato de hash.
     */
    @NotNull(message = "Senha é obrigatória.")
    @Size(min = 6, max = 15, message = "A senha deve possuir entre 6 e 15 caracteres.")
    private String senhaNovo;

    /**
     * Campo de senha utilizado para edição de um registro.
     */
    private String senhaEdicao;

    /**
     * Contrutor padrão.
     */
    public AdministradorMB() {
        super(Administrador.class, "/administracao/administrador", "/administracao/administrador");
        senhaNovo = "";
        senhaEdicao = "";
    }

    /**
     * Salvar ou alterar um registro.
     * 
     * @return caminho para redirecionamento do sistema.
     */
    @Override
    public String salvarEdicao() {

        if (super.view.isNovo()) {
            try {
                entidade.setSenha(Digest.hashString(senhaNovo, "SHA-256"));
                classeDAO.salvar(entidade);
                senhaNovo = "";
                senhaEdicao = "";
                Mensagem.mostrar("sucesso", "Sucesso", "Registro adicionado com sucesso.");
            } catch (PersistenceException | HashGenerationException ex) {
            }
        } else {
            try {
                if (!senhaEdicao.equals("")) {
                    entidade.setSenha(Digest.hashString(senhaEdicao, "SHA-256"));
                }
                classeDAO.alterar(entidade);
                senhaNovo = "";
                senhaEdicao = "";
                Mensagem.mostrar("sucesso", "Sucesso", "Registro alterado com sucesso.");
            } catch (PersistenceException | HashGenerationException ex) {
            }
        }

        this.entidade = novaEntidade();
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }
        view.reset();
        return view.getPaginaAtual();
    }

    /**
     * Remover um administrador do sistema.
     * Não permite remover todos os administradores. Ao menos um deles deve permanecer 
     * cadastrado no sistema.
     * 
     * @param entidade administrador a ser removido.
     * @return caminho para onde o sistema deve ser redirecionado.
     */
    @Override
    public String remover(Administrador entidade) {
        try {
            if (super.entidades.size() > 1) {
                classeDAO.excluir(entidade);
                Mensagem.mostrar("sucesso", "Sucesso", "Registro excluído com sucesso.");
            }else{
                Mensagem.mostrar("alerta", "Atenção", "Não é permitido excluir todos os administradores.");
            }
        } catch (PersistenceException ex) {
            Mensagem.mostrar("erro", "Erro", "Não foi possível fazer a exclusão porque há cadastros associados.");
        }

        //atualizar a listagem
        try {
            entidades = classeDAO.buscarTodos(classeEntidade);
        } catch (PersistenceException ex) {
            entidades = new ArrayList<>();
        }

        view.reset();
        return view.getPaginaAtual();
    }

    /**
     * 
     * @return senha do novo cadastro
     */
    public String getSenhaNovo() {
        return senhaNovo;
    }

    /**
     * 
     * @param senhaNovo senha do novo cadastro.
     */
    public void setSenhaNovo(String senhaNovo) {
        this.senhaNovo = senhaNovo;
    }

    /**
     * 
     * @return senha do registro em edição.
     */
    public String getSenhaEdicao() {
        return senhaEdicao;
    }

    /**
     * 
     * @param senhaEdicao senha do registro em edição.
     */
    public void setSenhaEdicao(String senhaEdicao) {
        this.senhaEdicao = senhaEdicao;
    }

}