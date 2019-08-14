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
package br.edu.ifpr.irati.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Resposta a uma questão de múltipla escolha.
 *
 * @author Valter Estevam
 */
@Entity(name = "respostamultiplaescolha")
@PrimaryKeyJoinColumn(name = "idResposta")
public class RespostaMultiplaEscolha extends Resposta implements Serializable{
    
    /**
     * Conjunto de opções escolhidas como resposta.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    protected Set<Opcao> opcoes;

    /**
     * Mapa relacionando objetos OpcaoResposta a Opções possíveis.
     * Opcao resposta marcará em um booleano se a opção foi escolhida ou não.
     * 
     * Este atributo só é utilizado pela interface gráfica sem marcado como @Transient
     */
    @Transient
    private Map<Opcao,OpcaoResposta> respostas;
    
    /**
     * Construtor padrão.
     */
    public RespostaMultiplaEscolha() {
        super();
        opcoes = new HashSet<>();
        respostas = new HashMap<>();
        
    }

    /**
     * Construtor.
     * 
     * @param opcoes conjunto de opções escolhidas como resposta.
     */
    public RespostaMultiplaEscolha(Set<Opcao> opcoes) {
        super();
        this.opcoes = opcoes;
    }

    /**
     * Construtor
     * 
     * @param opcoes conjunto de opções escolhidas como resposta.
     * @param idResposta identificador único.
     * @param dataHora data e hora da resposta.
     * @param questao questão respondida.
     * @param entrevistado entrevistado que respondeu.
     */
    public RespostaMultiplaEscolha(Set<Opcao> opcoes, int idResposta, Date dataHora, Questao questao, Entrevistado entrevistado) {
        super(idResposta, dataHora, questao, entrevistado);
        this.opcoes = opcoes;
    }
                
    /**
     * 
     * @return conjunto de opções escolhidas como resposta.
     */
    public Set<Opcao> getOpcoes() {
        return opcoes;
    }

    /**
     * 
     * @param opcoes conjunto de opções escolhidas como resposta.
     */
    public void setOpcoes(Set<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

    /**
     * 
     * @return mapa com a informação de quais opções foram escolhidas dentre as possiveis.
     */
    public Map<Opcao,OpcaoResposta> getRespostas() {
        return respostas;
    }

    /**
     * 
     * @param respostas mapa com a informação de quais opções foram escolhidas dentre as possiveis.
     */
    public void setRespostas(Map<Opcao,OpcaoResposta> respostas) {
        this.respostas = respostas;
    }
                    
}