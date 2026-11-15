CREATE TABLE produtos (
    id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    sku VARCHAR(50) NOT NULL,
    ean VARCHAR(13) NOT NULL,
    tipo_produto VARCHAR(30) NOT NULL,
    numero_figura VARCHAR(4) NOT NULL,
    ativo BOOLEAN NOT NULL,

    CONSTRAINT pk_produtos PRIMARY KEY (id),
    CONSTRAINT uk_produtos_sku UNIQUE (sku),
    CONSTRAINT uk_produtos_ean UNIQUE (ean),
    CONSTRAINT ck_produtos_ean CHECK (ean ~ '^[0-9]{13}$'),
    CONSTRAINT ck_produtos_tipo CHECK (
        tipo_produto IN (
            'FUNKO_NORMAL',
            'FUNKO_MEDIO',
            'FUNKO_GRANDE',
            'ACTION_FIGURE'
        )
    )
);