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
package br.edu.ifpr.irati.jmail.configuracao;

/**
 *
 * Representa os dados para estabelecer uma autenticação em um servidor de 
 * e-mail.
 * 
 * @author Valter Estevam
 */
public class Autenticacao {
    
    /**
     * E-mail para autenticação
     */
    private String usuario;
    
    /**
     * Senha para autenticação
     */
    private String senha;

    /**
     * Construtor padrão
     */
    public Autenticacao() {
        usuario = "";
        senha = "";
    }

    /**
     * Construtor completo.
     * 
     * @param usuario
     * @param senha 
     */
    public Autenticacao(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }
       
    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}