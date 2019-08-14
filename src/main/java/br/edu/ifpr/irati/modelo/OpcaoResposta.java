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

/**
 * Registra se uma opção foi escolhida como resposta.
 *
 * Não é persistido no banco de dados
 *
 * @author Valter Estevam
 */
public class OpcaoResposta implements Serializable {

    /**
     * True se foi marcada como resposta no questionário.
     */
    private boolean checked;

    /**
     * Contrutor padrão.
     */
    public OpcaoResposta() {
        checked = false;
    }

    /**
     * Construtor.
     * 
     * @param checked true se foi marcada como resposta.
     */
    public OpcaoResposta(boolean checked) {
        this.checked = checked;
    }

    /**
     * 
     * @return true se foi marcada como resposta.
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * 
     * @param checked true se foi marcada como resposta.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}