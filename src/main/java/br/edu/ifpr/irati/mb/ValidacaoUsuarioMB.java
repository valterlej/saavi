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
import br.edu.ifpr.irati.dao.EntrevistadoDAO;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Entrevistado;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * Controla a interface de validação de entrevistado quando o link de acesso encaminhado por
 * e-mail é acessado.
 * 
 * O login de um usuário só é validado se o link for acessado.
 * 
 * @author Valter Estevam
 */
@RequestScoped
@ManagedBean
public class ValidacaoUsuarioMB implements Serializable {

    /**
     * Entrevistado validado.
     */
    private Entrevistado entrevistado;   
    
    /**
     * Mensagem exibida em tala.
     */
    private String mensagem;
    
    /**
     * Rótulo do botão da interface.
     */
    private String labelBotao;
    
    /**
     * Objeto para acesso aos dados do entrevistado.
     */
    EntrevistadoDAO entrevistadoDAO;

    /**
     * Construtor padrão.
     */
    public ValidacaoUsuarioMB() {

        entrevistadoDAO = new EntrevistadoDAO();
        entrevistado = new Entrevistado();
        mensagem = "Usuário validado!";
        labelBotao = "Entrar no sistema!";
    }

    /**
     * Obtém o CPF informado à página pelo método HTTP GET e verifica se é possível ou não validar 
     */
    @PostConstruct
    public void validarEntrevistado() {

        String cpf = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        try {
            if (cpf != null) {
                //carrega o usuário correspondente ao cpf
                this.entrevistado = entrevistadoDAO.buscarPorCPF(cpf);
                
                //altera o marcador de autorização para verdadeiro
                this.entrevistado.setValidado(true);
                Dao<Entrevistado> entrevistadoDAO2 = new GenericDAO<>(Entrevistado.class);
                entrevistadoDAO2.alterar(entrevistado);                
            }else{
                mensagem = "Acesso não autorizado.";
                labelBotao = "Sair";
            }
        } catch (PersistenceException ex) {
            entrevistado = new Entrevistado();
            mensagem = "Usuário não validado! Entre em contato com o administrador.";
            labelBotao = "Sair";
        }

    }
    
    /**
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String entrar(){
        return "/index";
    }
    
    /**
     * 
     * @return mensagem de boas vindas com o nome do usuário.
     */
    public String getNomeUsuario(){
        return "Olá "+this.entrevistado.getNomeCompleto()+"!";
    }

    /**
     * 
     * @return entrevistado validado ou não.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado validado ou não.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }

    /**
     * 
     * @return mensagem exibida na tela.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * 
     * @param mensagem mensagem exibida na tela.
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * 
     * @return rótulo do botão da interface gráfica.
     */
    public String getLabelBotao() {
        return labelBotao;
    }

    /**
     * 
     * @param labelBotao rótulo do botão da interface gráfica.
     */
    public void setLabelBotao(String labelBotao) {
        this.labelBotao = labelBotao;
    }

}