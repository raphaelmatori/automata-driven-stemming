package org.stemmer.state.of.art.RSLP;

public class Exception{

	public static final String[]

	PLURAL_AES = {"mães"},
	PLURAL_AIS = {"cais", "mais"},
	PLURAL_EIS = {"leis"},
	PLURAL_RES = {"pires"},
	PLURAL_IS = {"bíquinis", "crúcis", "depois", "dois", "leis", "lápis", "pois"},
	PLURAL_S = {"aliás", "ambas", "ambos", "após", "através", "atrás", "cais", "convés", "crúcis", "fezes", "férias", "gás", "lápis", "mais", "mas", "menos", "messias", "moisés", "país", "pêsames"},

	FEMININE_ONA = {"abandona", "acetona", "carona", "cortisona", "detona", "iona", "lona", "maratona", "monótona"},
	FEMININE_NA = {"abandona", "acetona", "banana", "campana", "caravana", "carona", "cortisona", "detona", "grana", "guiana", "iona", "lona", "maratona", "monótona", "paisana"},
	FEMININE_INHA = {"linha", "minha", "rainha"},
	FEMININE_ESA = {"ilesa", "mesa", "obesa", "pesa", "presa", "princesa", "turquesa"},
	FEMININE_OSA = {"mucosa", "prosa"},
	FEMININE_ICA = {"dica"},
	FEMININE_ADA = {"pitada"},
	FEMININE_IDA = {"vida"},
	FEMININE_IIDA = {"recaída", "saída"},
	FEMININE_IMA = {"vítima"},
	FEMININE_IVA = {"oliva", "saliva"},
	FEMININE_EIRA = {"bandeira", "barreira", "beira", "besteira", "cadeira", "capoeira", "feira", "frigideira", "fronteira", "poeira"},
	FEMININE_A = {"amanhã", "arapuã", "divã", "fã"},

	ADVERB_MENTE = {"experimente"},

	DEGREE_INHO = {"caminho", "cominho"},
	DEGREE_AZIO = {"topázio"},
	DEGREE_ACO = {"antebraço"},
	DEGREE_ZAO = {"coalizão"},
	DEGREE_AO = {"aptidão", "barão", "bilhão", "camarão", "campeão", "canção", "capitão", "chimarrão", "colchão", "coração", "cordão", "cristão", "embrião", "espião", "estação", "falcão", "feição", "ficção", "fogão", "folião", "furacão", "fusão", "gamão", "glutão", "grotão", "ilusão", "lampião", "leilão", "leão", "limão", "macacão", "mamão", "melão", "milhão", "nação", "orgão", "patrão", "portão", "quinhão", "rincão", "senão", "tração", "órfão"},

	NOUN_ASTICO = {"eclesiástico"},
	NOUN_AMENTO = {"departamento", "firmamento", "fundamento"},
	NOUN_IDADE = {"autoridade", "comunidade"},
	NOUN_IZADO = {"organizado", "pulverizado"},
	NOUN_ATIVO = {"pejorativo", "relativo"},
	NOUN_ANCIA = {"ambulância"},
	NOUN_IZAC = {"organizaç"},
	NOUN_ARIO = {"aniversário", "armário", "diário", "lionário", "salário", "voluntário"},
	NOUN_ANTE = {"adiante", "elefante", "gigante", "instante", "possante", "restaurante"},
	NOUN_ORIA = {"categoria"},
	NOUN_INAL = {"afinal"},
	NOUN_AVEL = {"afável", "potável", "razoável", "vulnerável"},
	NOUN_IVEL = {"possível"},
	NOUN_ENTE = {"acrescente", "alimente", "aparente", "freqüente", "oriente", "permanente"},
	NOUN_TIVO = {"relativo"},
	NOUN_AGEM = {"carruagem", "chantagem", "coragem", "vantagem"},
	NOUN_IDOR = {"ouvidor"},
	NOUN_EIRO = {"desfiladeiro", "mosteiro", "pioneiro"},
	NOUN_ISMO = {"cinismo"},
	NOUN_ICO = {"explico", "público", "tico"},
	NOUN_ICE = {"cúmplice"},
	NOUN_URA = {"acupuntura", "costura", "imatura"},
	NOUN_UAL = {"bissexual", "pontual", "virtual", "visual"},
	NOUN_IVO = {"passivo", "pejorativo", "possessivo"},
	NOUN_ADO = {"grado"},
	NOUN_IDO = {"consolido", "cândido", "decido", "duvido", "marido", "rápido", "tímido"},
	NOUN_OSO = {"precioso"},
	NOUN_AC = {"equaç", "relaç"},
	NOUN_IC = {"eleiç"},
	NOUN_AL = {"animal", "bissexual", "desleal", "estatal", "fiscal", "formal", "liberal", "pessoal", "pontual", "postal", "sideral", "sucursal", "virtual", "visual"},
	NOUN_OR = {"assessor", "autor", "benfeitor", "favor", "melhor", "motor", "pastor", "redor", "rigor", "sensor", "tambor", "terior", "tumor"},

	VERB_AVAM = {"agravam"},
	VERB_ESTE = {"agreste", "faroeste"},
	VERB_IREI = {"admirei"},
	VERB_IREM = {"adquirem"},
	VERB_ARA = {"arara", "prepara"},
	VERB_ARAA = {"alvará"},
	VERB_ARE = {"prepare"},
	VERB_AVA = {"agrava"},
	VERB_ERA = {"acelera", "espera"},
	VERB_ERE = {"espere"},
	VERB_IAM = {"ampliam", "elogiam", "enfiam", "ensaiam"},
	VERB_IMO = {"intimo", "nimo", "queimo", "reprimo", "ximo", "íntimo"},
	VERB_IRA = {"fronteira", "sátira"},
	VERB_IRE = {"adquire"},
	VERB_EAR = {"alardear", "nuclear"},
	VERB_AR = {"azar", "bazaar", "patamar"},
	VERB_EM = {"alem", "virgem"},
	VERB_ER = {"pier", "éter"},
	VERB_EU = {"chapeu"},
	VERB_IA = {"acia", "aprecia", "arredia", "cheia", "elogia", "estória", "fatia", "lábia", "mania", "polícia", "praia", "ásia"},
	VERB_IR = {"freir"},

	VOWEL = {"bebê", "ásia", "ão"};

	public static boolean isException(String word, String[] exceptions){
		int start = 0, p = 0, status = 0;
		int end = exceptions.length - 1;
		while(start <= end){
			p = (end + start) / 2;
			status = word.compareTo(exceptions[p]);
			if(status == 0) return true;
			else if(status > 0) start = p + 1;
			else end = p - 1;
		}
		return false;
	}

}