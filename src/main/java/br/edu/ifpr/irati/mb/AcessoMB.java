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

import br.edu.ifpr.irati.configuracao.modelo.AutenticacaoEmail;
import br.edu.ifpr.irati.configuracao.modelo.PropriedadeEmail;
import br.edu.ifpr.irati.dao.AdministradorDAO;
import br.edu.ifpr.irati.dao.Dao;
import br.edu.ifpr.irati.dao.EntrevistadoDAO;
import br.edu.ifpr.irati.dao.GenericDAO;
import br.edu.ifpr.irati.exception.HashGenerationException;
import br.edu.ifpr.irati.exception.LoginException;
import br.edu.ifpr.irati.exception.MailerException;
import br.edu.ifpr.irati.exception.PersistenceException;
import br.edu.ifpr.irati.modelo.Administrador;
import br.edu.ifpr.irati.modelo.ConfiguracaoWeb;
import br.edu.ifpr.irati.modelo.Entrevistado;
import br.edu.ifpr.irati.mb.util.Digest;
import br.edu.ifpr.irati.mb.util.Mailer;
import br.edu.ifpr.irati.mb.util.Mensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * 
 * Controle da tela de acesso ao sistema e fornecedor de dados 
 * para o filtro de login.
 * 
 * @author Valter Estevam
 */
@ManagedBean
@SessionScoped
public class AcessoMB implements Serializable {

    /**
     * E-mail para uso no formulário de login.
     */
    private String email;
    
    /**
     * Senha para uso no formulário de login.
     */
    private String senha;

    /**
     * E-mail para uso no formulário de recuperação de senha.
     */
    private String emailRecuperacao;
    
    /**
     * Usada para armazenar uma nova senha gerada automaticamente.
     */
    private String novaSenha;

    /**
     * Usuário administrador do sistema.
     */
    private Administrador administrador;

    /**
     * Usuário entrevistado.
     */
    private Entrevistado entrevistado;

    /**
     * Informa se um administrador está logado no sistema.
     */
    private boolean administradorLogado;

    /**
     * Informa se um entrevistado está logado no sistema.
     */
    private boolean entrevistadoLogado;

    /**
     * Informa se os dados de acesso correspondente tanto a um administrador quanto a um entrevistado.
     */
    private boolean administradorEntrevistadoLogado;
    
    /**
     * Dados da configuração geral do sistema.
     */
    private ConfiguracaoWeb configuracao;

    /**
     * Objeto para acesso aos dados de configuração armazenados no banco de dados.
     */
    private Dao<ConfiguracaoWeb> configuracaoDAO;
    
    /**
     * Contrutor padrão.
     */
    public AcessoMB() {
                
        email = "";
        senha = "";        
        emailRecuperacao = "";
        inicializar();
        configuracao = new ConfiguracaoWeb();
        configuracaoDAO = new GenericDAO<>(ConfiguracaoWeb.class);
        try {
            configuracao = configuracaoDAO.buscarPorId(1);
        } catch (PersistenceException ex) {
            configuracao = new ConfiguracaoWeb();
        }
        
    }

    /**
     * Inicializa o sistema no primeiro acesso a ele.
     * 
     * - Cadastra as propriedades de envio de e-mail.
     * - Cadastra o administrador default.
     * - Cadastra uma configuração padrão do sistema.
     * 
     */
    public void inicializar() {

        try {
            //buscar pelos administradores
            Dao<Administrador> administradorDAO = new GenericDAO(Administrador.class);
            List<Administrador> administradores = administradorDAO.buscarTodos(Administrador.class);
            if (administradores.isEmpty()) {

                Dao<PropriedadeEmail> propriedadeEmailDAO = new GenericDAO<>(PropriedadeEmail.class);
                Dao<AutenticacaoEmail> autenticacaoEmailDAO = new GenericDAO<>(AutenticacaoEmail.class);
                Dao<ConfiguracaoWeb> configuracaoWebDAO = new GenericDAO<>(ConfiguracaoWeb.class);

                //criar a configuração de:
                //e-mail default
                //administrador default e
                //configuração web default
                List<PropriedadeEmail> propriedades = new ArrayList<>();
                propriedades.add(new PropriedadeEmail(0, "mail.transport.protocol", "smtp"));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.starttls.enable", "true"));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.host", "mail.valterestevam.com.br"));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.auth", "true"));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.user", "saavidemo@valterestevam.com.br"));
                propriedades.add(new PropriedadeEmail(0, "mail.debug", "false"));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.port", "587"));
                propriedades.add(new PropriedadeEmail(0, "proxySet", ""));
                propriedades.add(new PropriedadeEmail(0, "socksProxyHost", ""));
                propriedades.add(new PropriedadeEmail(0, "socksProxyPort", ""));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.socketFactory.port", ""));
                propriedades.add(new PropriedadeEmail(0, "mail.smtp.socketFactory.fallback", ""));
                AutenticacaoEmail autenticacaoEmail = new AutenticacaoEmail(0, "saavidemo@valterestevam.com.br", "saaviifpr2019");
                Administrador adm = new Administrador(0, "Administrador Geral", "saavidemo@valterestevam.com.br", Digest.hashString("admin", "SHA-256"));

                configuracao = new ConfiguracaoWeb(0, "Nome da Instituição", "Endereço da Instituição",
                        "Telefone da instituição", "E-mail para contato", "http://localhost:8080");

                for (PropriedadeEmail p : propriedades) {
                    propriedadeEmailDAO.salvar(p);
                }
                autenticacaoEmailDAO.salvar(autenticacaoEmail);
                administradorDAO.salvar(adm);
                configuracaoWebDAO.salvar(configuracao);
            }
        } catch (PersistenceException | HashGenerationException ex) {
        }

    }

    /**
     * Método para verificar os dados fornecidos no formulário de login.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String acessar() {

        try {
            //verificar se é um administrador, um entrevistado ou se corresponde aos dois
            AdministradorDAO administradorDAO = new AdministradorDAO();
            EntrevistadoDAO entrevistadoDAO = new EntrevistadoDAO();

            try {
                administrador = administradorDAO.buscarPorEmailSenha(email, Digest.hashString(senha, "SHA-256"));
            } catch (LoginException ex) {
                administrador = new Administrador();
            }

            try {
                entrevistado = entrevistadoDAO.buscarPorEmailSenhaValido(email, Digest.hashString(senha, "SHA-256"));
            } catch (LoginException ex) {
                entrevistado = new Entrevistado();
            }

            if (administrador.getIdAdministrador() == 0 && entrevistado.getIdEntrevistado() == 0) {
                Mensagem.mostrar("alerta", "Atenção", "Usuário ou senha inválido");
            } else if (administrador.getIdAdministrador() == 0 && entrevistado.getIdEntrevistado() > 0) {
                //login de um entrevistado
                entrevistadoLogado = true;
                administradorLogado = false;
                administradorEntrevistadoLogado = false;
                return "/entrevista/questionarios";
            } else if (administrador.getIdAdministrador() > 0 && entrevistado.getIdEntrevistado() == 0) {
                //login de um administrador
                entrevistadoLogado = false;
                administradorLogado = true;
                administradorEntrevistadoLogado = false;
                return "/administracao/administrador";
            } else {
                //o usuário utiliza as mesmas credenciais para acesso tanto como administrador quanto entrevistado
                entrevistadoLogado = false;
                administradorLogado = false;
                administradorEntrevistadoLogado = true;
                return "/index";
            }
        } catch (HashGenerationException ex) {
        }
        return "/index";

    }

    /**
     * Usado para escolher o perfil de administrador quando e-mail e senha liberam tanto os perfis de administrador quanto de entrevistado.
     * 
     * @return  caminho para a próxima página a exibir.
     */
    public String escolherPerfilAdministrador() {

        entrevistado = new Entrevistado();
        entrevistadoLogado = false;
        administradorLogado = true;
        administradorEntrevistadoLogado = false;
        return "/administracao/administrador";
    }

    /**
     * Usado para escolher o perfil de entrevistado quando e-mail e senha liberam tanto os perfis de administrador quanto de entrevistado.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String escolherPerfilEntrevistado() {

        administrador = new Administrador();
        entrevistadoLogado = true;
        administradorLogado = false;
        administradorEntrevistadoLogado = false;
        return "/entrevista/questionarios";
    }

    /**
     * Executado quando o usuário pede para recuperar senha informando um e-mail cadastrado no sistema.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String solicitarRecuperacao() {

        //buscar entrevistado pelo e-mail
        EntrevistadoDAO entrevistadoDAO = new EntrevistadoDAO();
        Dao<Entrevistado> entrevistadoDAO2 = new GenericDAO<>(Entrevistado.class);

        try {
            Entrevistado e = entrevistadoDAO.buscarPorEmailValido(emailRecuperacao);
            novaSenha = this.gerarSenha(6);
            e.setSenha(Digest.hashString(novaSenha, "SHA-256"));
            entrevistadoDAO2.alterar(e);

            try {
                Mailer mailer = new Mailer(emailRecuperacao, "Recuperação de senha - SAAVI", "Olá!\nSua nova senha para acesso ao SAAVI é\n\n" + novaSenha + "\nNão esqueça de alterá-la após o login.");
                new Thread(mailer).start();
            } catch (MailerException ex) {
                Mensagem.mostrar("erro", "Erro", "Houve um erro ao encaminhar o e-mail. Entre em contato com o adminstrador.");
            }
        } catch (PersistenceException | HashGenerationException ex) {
            Mensagem.mostrar("erro", "Erro", ex.getMessage());
            return "/recuperarsenha";
        }
        emailRecuperacao = "";
        Mensagem.mostrar("sucesso", "Atenção", "Enviando e-mail.");
        return "/index";
    }

    /**
     * Cancela o pedido de recuperação de senha.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String cancelarRecuperacao() {        
        this.emailRecuperacao = "";
        return "/index";
    }

    /**
     * Abre a tela de recuperação de senha.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String recuperarSenha() {
        return "recuperarsenha";
    }

    /**
     * Abre a tela de cadastro de novo entrevistado.
     * 
     * @return caminho para a próxima página a exibir.
     */
    public String novoEntrevistado() {
        return "novousuario";
    }

    
    /**
     * Cria uma senha alpha numérica de tamanho informado como parâmetro.
     * @param tamanho quantidade de caracteres da senha.
     * @return senha aleatória.
     */
    private String gerarSenha(int tamanho) {
        String[] carct = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        String s = "";

        for (int x = 0; x < tamanho; x++) {
            int j = (int) (Math.random() * carct.length);
            s += carct[j];
        }

        return s;
    }

    /**
     * 
     * @return texto a ser exibido no painel inicial do sistema.
     */
    public String getNomePainel(){
        return "Sistema de Autoavaliação Institucional - "+configuracao.getNomeInstituicao();
    }
    
    /**
     * 
     * @return e-mail para login.
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email e-mail para login.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return senha para login.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * 
     * @param senha senha para login.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * 
     * @return administrador logado.
     */
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * 
     * @param administrador administrador logado.
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    /**
     * 
     * @return entrevistado logado.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado logado.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }   
    
    /**
     * 
     * @return true se um administrador está logado.
     */
    public boolean isAdministradorLogado() {
        return administradorLogado;
    }

    /**
     * 
     * @param administradorLogado  true se um administrador está logado.
     */
    public void setAdministradorLogado(boolean administradorLogado) {
        this.administradorLogado = administradorLogado;
    }

    /**
     * 
     * @return true se um entrevistado está logado.
     */
    public boolean isEntrevistadoLogado() {
        return entrevistadoLogado;
    }

    /**
     * 
     * @param entrevistadoLogado true se um entrevistado está logado.
     */
    public void setEntrevistadoLogado(boolean entrevistadoLogado) {
        this.entrevistadoLogado = entrevistadoLogado;
    }

    /**
     * 
     * @return true se ambos estão logados.
     */
    public boolean isAdministradorEntrevistadoLogado() {
        return administradorEntrevistadoLogado;
    }

    /**
     * 
     * @param administradorEntrevistadoLogado true se ambos estão logados.
     */
    public void setAdministradorEntrevistadoLogado(boolean administradorEntrevistadoLogado) {
        this.administradorEntrevistadoLogado = administradorEntrevistadoLogado;
    }

    /**
     * 
     * @return e-mail informado para recuperação da senha.
     */
    public String getEmailRecuperacao() {
        return emailRecuperacao;
    }

    /**
     * 
     * @param emailRecuperacao e-mail informado para recuperação da senha.
     */
    public void setEmailRecuperacao(String emailRecuperacao) {
        this.emailRecuperacao = emailRecuperacao;
    }   

    /**
     * 
     * @return nova senha gerada.
     */
    public String getNovaSenha() {
        return novaSenha;
    }

    /**
     * 
     * @param novaSenha nova senha gerada.
     */
    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    /**
     * 
     * @return configuração geral do sistema.
     */
    public ConfiguracaoWeb getConfiguracao() {
        return configuracao;
    }

    /**
     * 
     * @param configuracao configuração geral do sistema.
     */
    public void setConfiguracao(ConfiguracaoWeb configuracao) {
        this.configuracao = configuracao;
    }
        
}