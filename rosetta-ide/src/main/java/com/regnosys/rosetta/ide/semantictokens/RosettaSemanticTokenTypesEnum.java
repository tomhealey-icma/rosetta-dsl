package com.regnosys.rosetta.ide.semantictokens;

public enum RosettaSemanticTokenTypesEnum implements ISemanticTokenType {
	// Predefined by the LS protocol:
	TYPE("type"),
	ENUM("enum"),
	PARAMETER("parameter"),
	VARIABLE("variable"),
	PROPERTY("property"),
	ENUM_MEMBER("enumMember"),
	FUNCTION("function"),
	// Custom ones:
	BASIC_TYPE("basicType"),
	RECORD_TYPE("recordType"),
	QUALIFIED_TYPE("qualifiedType"),
	CALCULATION_TYPE("calculationType"),
	DOCUMENT_CORPUS("documentCorpus"),
	DOCUMENT_SEGMENT("documentSegment"),
	META_MEMBER("metaMember"),
	INLINE_PARAMETER("inlineParameter"),
	RULE("rule");
	
	private final String value;
	
	RosettaSemanticTokenTypesEnum(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
}