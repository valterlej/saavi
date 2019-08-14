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

import br.edu.ifpr.irati.configuracao.modelo.AutenticacaoEmail;
import br.edu.ifpr.irati.configuracao.modelo.PropriedadeEmail;
import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.MailerException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.ConfiguracaoWeb;
import br.edu.ifpr.irati.mb.util.Mailer;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * Controlador da interface de gerenciamento de configurações.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class ConfiguracaoMB implements Serializable {

    /**
     * Objeto contendo as propriedades gerais de configuração do sistema.
     */
    private ConfiguracaoWeb configuracaoWeb;

    /**
     * Objeto contendo os dados de usuário e senha da conta de e-mail usada para envios automáticos.
     */
    private AutenticacaoEmail autenticacaoEnvioEmail;

    /**
     * Lista de propriedades para conexão com o servidor de e-mails.
     */
    private List<PropriedadeEmail> propriedadeEmails;

    /**
     * Objeto para acesso aos dados de configurações gerais.
     */
    private final Dao<ConfiguracaoWeb> configuracaoWebDao;
    
    /**
     * Objeto para acesso aos dados do objeto de autenticação de conta de e-mail.
     */
    private final Dao<AutenticacaoEmail> autenticacaoEmailDAO;
    
    /**
     * Objeto para acesso aos dados das propriedades para conexão com o servidor de e-mails.
     */
    private final Dao<PropriedadeEmail> propriedadeEmailDAO;

    /**
     * Endereço de e-mail do destinatário de teste de envio.
     */
    private String destinatarioTeste;

    /**
     * Construtor padrão.
     */
    public ConfiguracaoMB() {

        configuracaoWebDao = new GenericDAO<>(ConfiguracaoWeb.class);
        autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
        propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
        atualizarDados();
    }

    /**
     * Carrega os dados atualizados do sistema diretamente do banco de dados.
     */
    public void atualizarDados() {
        try {
            //buscar dados no banco
            configuracaoWeb = configuracaoWebDao.buscarPorId(1);
            autenticacaoEnvioEmail = autenticacaoEmailDAO.buscarPorId(1);
            propriedadeEmails = propriedadeEmailDAO.buscarTodos(PropriedadeEmail.class);
            destinatarioTeste = "";
        } catch (PersistenceException ex) {
        }
    }

    /**
     * Salva as novas configurações.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String salvar() {

        try {
            configuracaoWebDao.alterar(configuracaoWeb);
            autenticacaoEmailDAO.alterar(autenticacaoEnvioEmail);
            for (PropriedadeEmail p : propriedadeEmails) {
                propriedadeEmailDAO.alterar(p);
            }
            Mensagem.mostrar("sucesso", "Sucesso", "Registros atualizados com sucesso.");
        } catch (PersistenceException ex) {
        }
        return "/administracao/administrador";

    }

    /**
     * Cancelar a edição de configurações.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String cancelar() {
        return "/administracao/administrador";
    }

    /**
     * 
     * Realiza o envio de um e-mail de teste com as configurações presentes na 
     * interface gráfica antes que elas sejam armazenadas permanentemente no banco de dados.
     * 
     * @return caminho para o qual o sistema deve ser redirecionado.
     */
    public String enviarTeste() {

        if (this.destinatarioTeste.equals("")) {
            Mensagem.mostrar("erro", "Atenção", "Informe um endereço para envio do teste.");
        } else {
            try {
                Mensagem.mostrar("alerta", "Enviando...", "Enviando mensagem de teste.");
                
                Mailer mailer = new Mailer(propriedadeEmails, autenticacaoEnvioEmail,destinatarioTeste, "E-mail de teste SAAVI.", "Suas configurações de e-mail estão corretas.");
                
                new Thread(mailer).start();
                
                Mensagem.mostrar("alerta", "Aviso", "Mensagem encaminhada. Verifique no endereço informado se a mensagem chegou.");
            } catch (MailerException ex) {
                Mensagem.mostrar("erro", "Falha", ex.getMessage());
            }
        }

        return "/administracao/configuracao";
    }

    /**
     * 
     * @return configurações gerais do sistema.
     */
    public ConfiguracaoWeb getConfiguracaoWeb() {
        return configuracaoWeb;
    }

    /**
     * 
     * @param configuracaoWeb configurações gerais do sistema.
     */
    public void setConfiguracaoWeb(ConfiguracaoWeb configuracaoWeb) {
        this.configuracaoWeb = configuracaoWeb;
    }
   
    /**
     * 
     * @return dados para autenticação no servidor de e-mail.
     */
    public AutenticacaoEmail getAutenticacaoEnvioEmail() {
        return autenticacaoEnvioEmail;
    }

    /**
     * 
     * @param autenticacaoEnvioEmail dados para autenticação no servidor de e-mail.
     */
    public void setAutenticacaoEnvioEmail(AutenticacaoEmail autenticacaoEnvioEmail) {
        this.autenticacaoEnvioEmail = autenticacaoEnvioEmail;
    }

    /**
     * 
     * @return relação de propriedades para envio de e-mails.
     */
    public List<PropriedadeEmail> getPropriedadeEmails() {
        return propriedadeEmails;
    }

    /**
     * 
     * @param propriedadeEmails relação de propriedades para envio de e-mails.
     */
    public void setPropriedadeEmails(List<PropriedadeEmail> propriedadeEmails) {
        this.propriedadeEmails = propriedadeEmails;
    }

    /**
     * 
     * @return endereço do destinatário para envio do e-mail de teste.
     */
    public String getDestinatarioTeste() {
        return destinatarioTeste;
    }

    /**
     * 
     * @param destinatarioTeste endereço do destinatário para envio do e-mail de teste.
     */
    public void setDestinatarioTeste(String destinatarioTeste) {
        this.destinatarioTeste = destinatarioTeste;
    }

}