#include <cstdlib>
#include <cstdio>
#include <cstring>

void copiaBytePorByte( FILE* finn, FILE* fout );
void copiaCompleta( FILE* finn, FILE* fout );

int main(int argc, char **argv)
{
	FILE* finn = NULL;
	FILE* fout = NULL;
	int code = 0;
	// validar parâmetros de linha de comando
	if( argc != 4 )
	{
		fprintf(stderr, "Parâmetros incorretos:\n");
		fprintf(stderr, "appendMsg \"sua mensagem\" arquivo_entrada arquivo_saida\n");

		code = 1;
		goto cleanExit;
	}

	/*
	argv[0] --> nome do programa
	argv[1] --> mensagem
	argv[2] --> arquivo de entrada
	argv[3] --> arquivo de saída
	*/
	if( strcmp(argv[2], argv[3]) == 0 )
	{
		fprintf(stderr, "Os arquivos de entrada e saída são os mesmos!\n");

		code = 2;
		goto cleanExit;
	}

	finn = fopen( argv[2] , "r");
	if( finn == NULL )
	{
		fprintf(stderr, "Arquivo de entrada inexistente ou inacessível!\n");

		code = 3;
		goto cleanExit;
	}

	fout = fopen( argv[3] , "r");
	if( fout != NULL )
	{
		char c = 'S';
		printf("\nArquivo de saída já existe. Sobrescrever (N para cancelar)? \n");

		scanf("%c", &c);

		if( c == 'n' || c == 'N' )
		{
			printf("\toperação cancelada pelo usuário\n");
			code = 4;
			goto cleanExit;
		}

		// fecha o arquivo
		fclose(fout);
		fout = NULL;
	}

	// cópia para valer! --- abrindo o arquivo
	fout = fopen( argv[3] , "w");
	if( fout == NULL )
	{
		fprintf(stderr, "Não foi possível abrir o arquivo de saída!\n");

		code = 5;
		goto cleanExit;
	}

	// lendo os dados do primeiro arquivo
#if 0
	copiaBytePorByte( finn, fout );
#else
	copiaCompleta( finn, fout );
#endif

	// finalmente, colocando a mensagem no final do arquivo
	fprintf( fout, "\n%s", argv[1] );

cleanExit:
	if( finn ) fclose(finn);
	if( fout ) fclose(fout);

	return code;
}

void copiaBytePorByte( FILE* finn, FILE* fout )
{
#if 0
	// Opção 1: usando fgetc()
	// DICA: evite usar feof(), pois o término do arquivo é "consumido" por fgetc(),
	//       o que leva a um laço infinito!
	int i;

	while( (i = fgetc(finn)) != EOF ) // enquanto o arquivo de entrada tiver bytes
	{
		fputc( i, fout );
	}
#else

	char c;
	while( !feof(finn) )
	{
		if( fread( &c, sizeof(char), 1, finn ) )
		{
			fwrite( &c, sizeof(char), 1, fout );
		}
	}

#endif
}

void copiaCompleta( FILE* finn, FILE* fout )
{
	// calcula o tamanho do arquivo de entrada
	size_t tamanho = 0;

	fseek( finn, 0, SEEK_END ); // vai para o final do arquivo
	tamanho = ftell( finn ); // diz a posição em bytes, que é justamente o tamanho do arquivo
	fseek( finn, 0, SEEK_SET ); // volta para o começo ("rebobina")

	// só faz sentido copiar aquivos que não estejam vazios!
	if( tamanho > 0 )
	{
		char* data = (char*) malloc( tamanho );

#if 0
		// opção A) leitura "inocente"
		fread( data, sizeof(char), tamanho, finn );
		fwrite( data, sizeof(char), tamanho, fout );
#else
		// opção B) leitura menos inocente, contando que fread() pode não
		//          não conseguir ler todos os bytes de uma só vez
		while( tamanho )
		{
			size_t lidos = fread( data, sizeof(char), tamanho, finn );

			fwrite( data, sizeof(char), lidos, fout );

			// "desconta" o que leu do que falta ler
			tamanho -= lidos;
		}

#endif

		free(data);
	}
}

