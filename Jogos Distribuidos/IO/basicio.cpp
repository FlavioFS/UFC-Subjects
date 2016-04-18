#include <cstdlib>
#include <cstdio>
#include <cstring>

void blockCopy (FILE *finn, FILE *fout);

int main(int argc, char const *argv[])
{
	FILE* finn = NULL;
	FILE* fout = NULL;
	int rcode = 0;

	// Counting args
	if (argc != 3)
	{
		fprintf(stderr, "Parâmetros incorretos:\n");
		fprintf(stderr, "appendMsg \"sua mensagem\" arquivo_entrada arquivo_saída\n");
		rcode = 1;
		goto closeFiles;
	}

	// Different args?
	if ( strcmp(argv[2], argv[3]) )
	{
		fprintf(stderr, "Os arquivos de entrada e saída são os mesmos!\n");
		rcode = 2;
		goto closeFiles;
	}

	// Valid input?
	finn = fopen(argv[2], "r");
	if (finn == NULL)
	{
		fprintf(stderr, "Entrada inexistente ou inacessível!\n");
		rcode = 3;
		goto closeFiles;
	}

	// Valid output?
	fout = fopen(argv[3], "w");
	if (fout != NULL)
	{
		fprintf(stderr, "Arquivo de saída já existe. Escolha outro!\n");
		rcode = 4;
		goto closeFiles;
	}

	rcode = 0;


closeFiles:
	if (finn) fclose(finn);
	if (fout) fclose(fout);

	return rcode;
}

void blockCopy (FILE *finn, FILE *fout) {
	// File Size
	size_t fSize = 0;
	int i = 0;

	// Writing
	fseek(finn, 0, SEEK_END);
	fSize = ftell(finn);
	fseek(finn, 0, SEEK_SET);

	if (fSize > 0) {
		char *dados = (char*) malloc (fSize);

		while (fSize) {
			size_t loaded = fread(dados, sizeof(char), fSize, finn);

			fwrite(dados, sizeof(char), loaded, fout);

			fSize -= loaded;
		}

		free(dados);

	}
}