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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Resposta a uma questão.
 *
 * @author Valter Estevam
 */
@Entity (name="resposta")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Resposta implements Serializable{

    /**
     * Identificador único.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected int idResposta;
    
    /**
     * Data e hora da resposta.
     */
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataHora;
    
    /**
     * Questão respondida.
     */
    @OneToOne
    protected Questao questao;
    
    /**
     * Entrevistado que respondeu.
     */
    @OneToOne
    private Entrevistado entrevistado;
    
    /**
     * Construtor padrão.
     */    
    protected Resposta() {
        idResposta = 0;
        dataHora = new Date();
        entrevistado = new Entrevistado();
    }

    /**
     * Construtor.
     * 
     * @param idResposta identificador único.
     * @param dataHora data e hora da resposta.
     * @param questao questão respondida.
     * @param entrevistado entrevistado que respondeu.
     */
    protected Resposta(int idResposta, Date dataHora, Questao questao, Entrevistado entrevistado) {
        this.idResposta = idResposta;
        this.dataHora = dataHora;
        this.questao = questao;
        this.entrevistado = entrevistado;
    }
   
    /**
     * 
     * @return identificador único.
     */
    public int getIdResposta() {
        return idResposta;
    }

    /**
     * 
     * @param idResposta identificador único.
     */
    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    /**
     * 
     * @return data e hora da resposta.
     */
    public Date getDataHora() {
        return dataHora;
    }

    /**
     * 
     * @param dataHora data e hora da resposta.
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * 
     * @return questão respondida.
     */
    public Questao getQuestao() {
        return questao;
    }

    /**
     * 
     * @param questao questão respondida.
     */
    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    /**
     * 
     * @return entrevistado que respondeu.
     */
    public Entrevistado getEntrevistado() {
        return entrevistado;
    }

    /**
     * 
     * @param entrevistado entrevistado que respondeu.
     */
    public void setEntrevistado(Entrevistado entrevistado) {
        this.entrevistado = entrevistado;
    }

    /**
     * Método usado para verificar a igualdade entre objetos.
     * A comparação é feita pelo id.
     * 
     * @param obj objeto a ser comparado.
     * @return true se forem iguais.
     */
    @Override
    public boolean equals(Object obj) {
        Resposta r = (Resposta) obj;
        return this.idResposta == r.getIdResposta();
    }
                  
}