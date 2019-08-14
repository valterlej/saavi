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

import br.edu.ifpr.irati.jmail.exception.SendMailException;
import java.util.List;

/**
 * Classe responsável por representar uma configuração de conexão e envio 
 * de e-mails pelo sistema.
 * 
 * 
 * @author Valter Estevam
 */
public class Configuracao {

    /**
     * Caminho para o arquivo de configuração
     */
    private String arquivoConfiguracao;

    /**
     * Relação de configurações de conexão presentes no arquivo     
     */
    private List<Conexao> conexoes;

    /**
     * Construtor padrão que considera o arquivo conf.xml interno à API
     * 
     * @throws SendMailException 
     */
    public Configuracao() throws SendMailException {
        try {
            ParserConf parser = new ParserConf("src/conf.xml");
            conexoes = parser.getConexoes();
        } catch (Exception ex) {
            throw new SendMailException(ex.getMessage());
        }
    }

    /**
     * Construtor que recebe o caminho para um arquivo de configuração que 
     * pode estar externo à API (e.g. em um projeto web)
     * 
     * @param arquivoConfiguracao
     * @throws SendMailException 
     */
    public Configuracao(String arquivoConfiguracao) throws SendMailException {
        try {
            this.arquivoConfiguracao = arquivoConfiguracao;
            ParserConf parser = new ParserConf(arquivoConfiguracao);
            conexoes = parser.getConexoes();
        } catch (Exception ex) {
            throw new SendMailException(ex.getMessage());
        }
    }
    
    /**
     * Construtor que recebe a lista de objetos de parâmetros de conexão diretamente.
     * @param conexoes 
     */
    public Configuracao(List<Conexao> conexoes){
        this.conexoes = conexoes;
    }

    /**
     * @return the arquivoConfiguracao
     */
    public String getArquivoConfiguracao() {
        return arquivoConfiguracao;
    }

    /**
     * @param arquivoConfiguracao the arquivoConfiguracao to set
     */
    public void setArquivoConfiguracao(String arquivoConfiguracao) {
        this.arquivoConfiguracao = arquivoConfiguracao;
    }

    /**
     * @return the conexoes
     */
    public List<Conexao> getConexoes() {
        return conexoes;
    }

    /**
     * @param conexoes the conexoes to set
     */
    public void setConexoes(List<Conexao> conexoes) {
        this.conexoes = conexoes;
    }

}