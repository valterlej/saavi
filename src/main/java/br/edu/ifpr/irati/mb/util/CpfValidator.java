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
package br.edu.ifpr.irati.mb.util;

import java.util.InputMismatchException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class CpfValidator implements Validator {

    /**
     * Método chamado pela API do JSF para realizar a validação do campo de CPF.
     * 
     * @param arg0 faces context
     * @param arg1 componente da interface
     * @param valorTela valor do campo - CPF
     * @throws ValidatorException 
     */
    @Override
    public void validate(FacesContext arg0, UIComponent arg1, Object valorTela)
            throws ValidatorException {

        valorTela = remove(String.valueOf(valorTela));

        if (validaCPF(String.valueOf(valorTela)) != true) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("CPF inválido!");

            throw new ValidatorException(message);
        }
    }

    /**
     * Validar um CPF informado.
     * 
     * @param CPF cadastro de pessoa física.
     * @return true se o cpf for válido.
     */
    private static boolean validaCPF(String CPF) {

        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || CPF.length() != 11 || CPF.length() == 14) {

            return (false);
        } else {

            char dig10, dig11;
            int sm, i, r, num, peso;

            // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
            try {
                // Calculo do 1o. Digito Verificador
                sm = 0;
                peso = 10;
                for (i = 0; i < 9; i++) {
                    // converte o i-esimo caractere do CPF em um numero:
                    // por exemplo, transforma o caractere '0' no inteiro 0        
                    // (48 eh a posicao de '0' na tabela ASCII)        
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11)) {
                    dig10 = '0';
                } else {
                    dig10 = (char) (r + 48); // converte no respectivo caractere numerico
                }
                // Calculo do 2o. Digito Verificador
                sm = 0;
                peso = 11;
                for (i = 0; i < 10; i++) {
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11)) {
                    dig11 = '0';
                } else {
                    dig11 = (char) (r + 48);
                }

                // Verifica se os digitos calculados conferem com os digitos informados.
                if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {

                    return true;
                } else {

                    return (false);
                }
            } catch (InputMismatchException erro) {

                return (false);
            }

        }
    }

    /**
     * Remover pontos e traço do CPF.
     * 
     * @param CPF cadastro de pessoa física.
     * @return CPF sem pontos e traço.
     */
    public static String remove(String CPF) {
        CPF = CPF.replace(".", "");
        CPF = CPF.replace("-", "");

        return CPF;
    }

}