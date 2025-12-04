package com.cnpj.cnpjsearch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cnpj.cnpjsearch.R
import com.cnpj.cnpjsearch.components.InfoRow
import com.cnpj.cnpjsearch.components.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()


    var cnpjInput by remember { mutableStateOf("") }
    val regex = Regex("^(?:\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2})$")

    val isValid = cnpjInput.matches(regex)



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 46.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Consulta CNPJ", fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    16.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(28.dp))
                    OutlinedTextField(
                        value = cnpjInput,
                        onValueChange = { cnpjInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        label = { Text("CNPJ") },
                        placeholder = { Text("Ex: 19131243000197") },
                        isError = cnpjInput.isNotBlank() && !isValid,
                        singleLine = true,
                        supportingText = {
                            if (cnpjInput.isNotBlank() && !isValid)
                                Text("Formato Incorreto")
                        }
                    )

                    Spacer(modifier = Modifier.height(38.dp))
                    Button(
                        onClick = { viewModel.searchData(cnpjInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        enabled = cnpjInput.isNotBlank() && isValid,
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF43A047)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            12.dp
                        ),
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        if (uiState is UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(22.dp), // tamanho bom para botão
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.search),
                                contentDescription = "Localized description",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Pesquisar")
                        }
                    }
                    Spacer(modifier = Modifier.padding(bottom = 18.dp))
                }
            }
        }
        when (uiState) {
            is UiState.Idle -> cnpjInput
            is UiState.Loading -> {}
            is UiState.Success -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    BasicAlertDialog(
                        onDismissRequest = { viewModel.clearState() }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                // --- Cabeçalho ---
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = (uiState as UiState.Success).data.razao_social,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    OutlinedButton(
                                        onClick = { viewModel.clearState() },
                                    ) {
                                        Text("FECHAR")
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "CNPJ: ${(uiState as UiState.Success).data.cnpj}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(Modifier.height(12.dp))

                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                // ===== Dados Básicos =====
                                SectionTitle("Dados Básicos")

                                InfoRow(
                                    "Razão Social",
                                    (uiState as UiState.Success).data.razao_social
                                )
                                InfoRow(
                                    "Nome Fantasia",
                                    (uiState as UiState.Success).data.nome_fantasia
                                )
                                InfoRow(
                                    "Situação Cadastral",
                                    (uiState as UiState.Success).data.descricao_situacao_cadastral
                                )
                                InfoRow(
                                    "Motivo Situação",
                                    (uiState as UiState.Success).data.motivo_situacao_cadastral.toString()
                                )
                                InfoRow(
                                    "Data Situação Cadastral",
                                    (uiState as UiState.Success).data.data_situacao_cadastral
                                )
                                InfoRow("Porte", (uiState as UiState.Success).data.porte)
                                InfoRow(
                                    "Natureza Jurídica",
                                    (uiState as UiState.Success).data.natureza_juridica
                                )

                                Spacer(Modifier.height(12.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                // ===== Endereço =====
                                SectionTitle("Endereço")

                                InfoRow(
                                    "Tipo de Logradouro",
                                    (uiState as UiState.Success).data.descricao_tipo_de_logradouro
                                )
                                InfoRow(
                                    "Logradouro",
                                    (uiState as UiState.Success).data.logradouro
                                )
                                InfoRow("Número", (uiState as UiState.Success).data.numero)
                                InfoRow(
                                    "Complemento",
                                    (uiState as UiState.Success).data.complemento
                                )
                                InfoRow("Bairro", (uiState as UiState.Success).data.bairro)
                                InfoRow("Município", (uiState as UiState.Success).data.municipio)
                                InfoRow("UF", (uiState as UiState.Success).data.uf)
                                InfoRow("CEP", (uiState as UiState.Success).data.cep)

                                Spacer(Modifier.height(12.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                // ===== Contato =====
                                SectionTitle("Contato")

                                InfoRow(
                                    "Telefone 1",
                                    (uiState as UiState.Success).data.ddd_telefone_1
                                )
                                InfoRow("Fax", (uiState as UiState.Success).data.ddd_fax)

                                Spacer(Modifier.height(12.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                // ===== CNAEs =====
                                SectionTitle("Atividade Principal")

                                InfoRow(
                                    "CNAE Fiscal",
                                    (uiState as UiState.Success).data.cnae_fiscal.toString()
                                )
                                InfoRow(
                                    "Descrição",
                                    (uiState as UiState.Success).data.cnae_fiscal_descricao
                                )


                                Spacer(Modifier.height(12.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(12.dp))

                                // ===== QSA =====
                                SectionTitle("Quadro Societário (QSA)")

                                (uiState as UiState.Success).data.qsa.forEach { socio ->
                                    InfoRow("Nome", socio.nome_socio)
                                    InfoRow("Qualificação", socio.qualificacao_socio)
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }

            is UiState.Failure -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    BasicAlertDialog(
                        onDismissRequest = { viewModel.clearState() }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Ocorreu um erro", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = { viewModel.clearState() }
                                ) {
                                    Text(text = "FECHAR")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
