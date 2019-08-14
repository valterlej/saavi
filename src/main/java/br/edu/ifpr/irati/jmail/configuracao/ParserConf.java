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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Classe responsável por ler e interpretar o arquivo de configuração de conexões.
 * 
 * @author Valter Estevam
 */
public class ParserConf {

    private String caminho;
    private List<Conexao> conexoes;

    public ParserConf(String caminho) throws Exception {
        this.caminho = caminho;
        conexoes = lerConfiguracoes();
    }

    /**
     * Método responsável por parsear o arquivo xml.
     * 
     * @return Lista de configurações de conexão.
     * @throws Exception 
     */
    private List<Conexao> lerConfiguracoes() throws Exception {

        List<Conexao> listaConexoes = new ArrayList<>();
        try {
            File f = new File(caminho);

            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(f);

            Element root = doc.getRootElement();

            List<Element> conexoes = root.getChildren();

            for (Element conexao : conexoes) {

                List<Propriedade> listaPropriedades = new ArrayList<>();

                Element propriedades = conexao.getChild("propriedades");

                Element autenticacao = conexao.getChild("autenticacao");
                
                List<Element> propriedadeElemList = propriedades.getChildren("propriedade");
                
                for (Element propriedade : propriedadeElemList) {

                    String nome = propriedade.getChildText("nome");
                    String valor = propriedade.getChildText("valor");
                    listaPropriedades.add(new Propriedade(nome, valor));
                }
                
                String usuario = autenticacao.getChildText("usuario");
                String senha = autenticacao.getChildText("senha");
                Autenticacao autent = new Autenticacao(usuario, senha);
                
                Conexao con = new Conexao(listaPropriedades, autent);
                listaConexoes.add(con);
            }
            return listaConexoes;
        } catch (IOException ex){
            throw new Exception("Arquivo não encontrado!");
        } catch (JDOMException ex) {
            throw new Exception("Falha ao interpretar o arquivo.");
        }
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