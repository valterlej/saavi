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
package br.edu.ifpr.irati.mb;

import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.MailerException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Campus;
import br.edu.ifpr.irati.modelo.Categoria;
import br.edu.ifpr.irati.modelo.ConfiguracaoWeb;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.mb.util.Mailer;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * Controlador para a tela de novo entrevistado/usuário.
 *
 * @author Valter Estevam
 */
@ManagedBean
@RequestScoped
public class NovoEntrevistadoMB implements Serializable {

    /**
     * Novo entrevistado.
     */
    private Entrevistado novoEntrevistado;

    /**
     * Listagem de campus.
     */
    private List<Campus> campi;
    
    /**
     * Listagem de categorias.
     */
    private List<Categoria> categorias;

    /**
     * Objeto para acesso aos dados de campus.
     */
    private Dao<Campus> campusDAO;
    
    /**
     * Objeto para acesso aos dados de categoria.
     */
    private Dao<Categoria> categoriaDAO;
    
    /**
     * Objeto para acesso aos dados de entrevistado.
     */
    private Dao<Entrevistado> entrevistadoDAO;

    /**
     * Senha informada no formulário de cadastro.
     */
    @NotNull(message = "Senha é obrigatória.")
    @Size(min = 6, max = 15, message = "A senha deve possuir entre 6 e 15 caracteres.")
    private String senha;

    /**
     * CPF informado no formulário de cadastro.
     */
    @NotNull(message = "CPF é obrigatório.")
    @Size(min = 14, max = 14, message = "Preencha o CPF.")
    private String cpf;

    /**
     * Construtor padrão.
     */
    public NovoEntrevistadoMB() {

        campusDAO = new GenericDAO<>(Campus.class);
        categoriaDAO = new GenericDAO<>(Categoria.class);
        entrevistadoDAO = new GenericDAO<>(Entrevistado.class);

        novoEntrevistado = new Entrevistado();

        try {
            campi = campusDAO.buscarTodos(Campus.class);
        } catch (PersistenceException ex) {
            campi = new ArrayList<>();
        }

        try {
            categorias = categoriaDAO.buscarTodos(Categoria.class);
        } catch (PersistenceException ex) {
            categorias = new ArrayList<>();
        }

        senha = "";
        cpf = "";

    }

    /**
     * Cancelar cadastro de novo entrevistado.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelarCadastro() {
        senha = "";
        cpf = "";
        return "index";
    }

    /**
     * Salvar o novo entrevistado e encaminhar e-mail com link para liberação do acesso ao sistema.
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String salvarEntrevistado() {

        try {
            //Salvar no banco de dados
            //criar resumo SHA-256 para senha e cpf
            novoEntrevistado.setCpf(Digest.hashString(cpf, "SHA-256"));
            novoEntrevistado.setSenha(Digest.hashString(senha, "SHA-256"));
            novoEntrevistado.setValidado(false); //alterar para false quando estiver encaminhando o e-mail.
            entrevistadoDAO.salvar(novoEntrevistado);

            /* 
                Enviar e-mail pedindo a confirmação da conta.
             */
            senha = "";
            cpf = "";

            //obter o endereço do servidor
            Dao<ConfiguracaoWeb> configuracaoWebDAO = new GenericDAO<>(ConfiguracaoWeb.class);
            ConfiguracaoWeb configuracaoWeb = configuracaoWebDAO.buscarPorId(1);
            
            
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
            String link = configuracaoWeb.getEnderecoServidor() + contextPath + "/validacaoacesso.xhtml?id=" + novoEntrevistado.getCpf();

            String mensagem = "Olá " + novoEntrevistado.getNomeCompleto() + 
                    "!\nRecebemos um cadastro em seu nome no SAAVI. Clique no link a seguir para validar seu usuário.\n\n <a href=\""+link+"\" target=\"_blank\">" + link + "</a>";
            Mailer mailer = new Mailer(novoEntrevistado.getEmail(), "Validação de acesso - SAAVI", mensagem);
            new Thread(mailer).start();

            Mensagem.mostrar("sucesso","Atenção","Acesse seu e-mail e clique no link de validação.");
            
        } catch (PersistenceException ex) {
            Mensagem.mostrar("erro", "Atenção", "O CPF ou o e-mail já encontram-se em nossa base de dados. Entre em contato com o administrador.");            
        } catch (MailerException ex) {
            Mensagem.mostrar("erro", "Atenção", "Falha ao encaminhar o e-mail. Entre em contato com o administrador.");
        } catch (HashGenerationException ex) {
        }
        return "index";
    }

    /**
     * 
     * @return novo entrevistado.
     */
    public Entrevistado getNovoEntrevistado() {
        return novoEntrevistado;
    }

    /**
     * 
     * @param novoEntrevistado novo entrevistado.
     */
    public void setNovoEntrevistado(Entrevistado novoEntrevistado) {
        this.novoEntrevistado = novoEntrevistado;
    }

    /**
     * 
     * @return listagem de campus.
     */
    public List<Campus> getCampi() {
        return campi;
    }

    /**
     * 
     * @param campi listagem de campus.
     */
    public void setCampi(List<Campus> campi) {
        this.campi = campi;
    }

    /**
     * 
     * @return listagem de categorias.
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * 
     * @param categorias listagem de categorias.
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * 
     * @return senha informada no formulário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 
     * @param senha senha informada no formulário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * 
     * @return CPF informado no formulário.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * 
     * @param cpf CPF informado no formulário.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}