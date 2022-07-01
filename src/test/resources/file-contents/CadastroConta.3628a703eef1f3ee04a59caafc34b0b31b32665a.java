package br.ufpe.cin;

import java.util.UUID;

public class CadastroConta {
  public Conta criarConta(Pessoa titular) {
    if (!titular.isMaiorIdade()) {
      throw new IllegalArgumentException("O titular da conta deve ser maior de idade!");
    }
    final Conta conta = new Conta(UUID.randomUUID(), titular);
    return conta;
  }
}
