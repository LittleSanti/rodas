package com.samajackun.rodas.sql.tokenizer;

import java.util.HashSet;
import java.util.Set;

public final class SqlTokenTypes
{
	public static final String SEMICOLON="rodas.sql.semicolon";

	public static final String ASTERISK="rodas.sql.asterisk";

	public static final String OPERATOR_PLUS="rodas.sql.operator_plus";

	public static final String OPERATOR_MINUS="rodas.sql.operator_minus";

	public static final String OPERATOR_DIV="rodas.sql.operator_div";

	public static final String COMMA="rodas.sql.comma";

	public static final String BRACKET_START="rodas.sql.bracket_start";

	public static final String BRACKET_END="rodas.sql.bracket_end";

	public static final String PARENTHESIS_START="rodas.sql.parenthesis_start";

	public static final String PARENTHESIS_END="rodas.sql.parenthesis_end";

	public static final String OPERATOR_EQUALS="rodas.sql.operator_equals";

	public static final String OPERATOR_DISTINCT="rodas.sql.operator_distinct";

	// public static final String OPERATOR_DISTINCT2="rodas.sql.operator_distinct2";

	public static final String OPERATOR_LOWER="rodas.sql.operator_lower";

	public static final String OPERATOR_LOWER_OR_EQUALS="rodas.sql.operator_lower_or_equals";

	public static final String OPERATOR_GREATER="rodas.sql.operator_greater";

	public static final String OPERATOR_GREATER_OR_EQUALS="rodas.sql.operator_greater_or_equals";

	public static final String OPERATOR_OR="rodas.sql.operator_or";

	public static final String OPERATOR_AND="rodas.sql.operator_and";

	public static final String OPERATOR_NOT="rodas.sql.operator_not";

	public static final String OPERATOR_IS="rodas.sql.operator_is";

	public static final String OPERATOR_LIKE="rodas.sql.operator_like";

	public static final String OPERATOR_IN="rodas.sql.operator_in";

	public static final String OPERATOR_ANY="rodas.sql.operator_any";

	public static final String OPERATOR_ALL="rodas.sql.operator_all";

	public static final String OPERATOR_SOME="rodas.sql.operator_some";

	public static final String OPERATOR_EXISTS="rodas.sql.operator_exists";

	public static final String INTEGER_NUMBER_LITERAL="rodas.sql.integer_number_literal";

	public static final String DECIMAL_NUMBER_LITERAL="rodas.sql.decimal_number_literal";

	public static final String TEXT_LITERAL="rodas.sql.text_literal";

	public static final String DOUBLE_QUOTED_TEXT_LITERAL="rodas.sql.double_quoted_text_literal";

	public static final String COMMENT="rodas.sql.comment";

	public static final String IDENTIFIER="rodas.sql.identifier";

	public static final String KEYWORD_SELECT="rodas.sql.keyword_select";

	public static final String KEYWORD_FROM="rodas.sql.keyword_from";

	public static final String KEYWORD_WHERE="rodas.sql.keyword_where";

	public static final String KEYWORD_GROUP="rodas.sql.keyword_group";

	public static final String KEYWORD_HAVING="rodas.sql.keyword_having";

	public static final String KEYWORD_ORDER="rodas.sql.keyword_order";

	public static final String KEYWORD_AS="rodas.sql.keyword_as";

	public static final String KEYWORD_BY="rodas.sql.keyword_by";

	public static final String KEYWORD_ALL="rodas.sql.keyword_all";

	public static final String KEYWORD_DISTINCT="rodas.sql.keyword_distinct";

	public static final String KEYWORD_DISTINCTROW="rodas.sql.keyword_distinctrow";

	public static final String KEYWORD_ASC="rodas.sql.keyword_asc";

	public static final String KEYWORD_DESC="rodas.sql.keyword_desc";

	public static final String KEYWORD_FOR="rodas.sql.keyword_for";

	public static final String KEYWORD_INNER="rodas.sql.keyword_inner";

	public static final String KEYWORD_JOIN="rodas.sql.keyword_join";

	public static final String KEYWORD_LEFT="rodas.sql.keyword_left";

	public static final String KEYWORD_RIGHT="rodas.sql.keyword_right";

	public static final String KEYWORD_ON="rodas.sql.keyword_on";

	public static final String KEYWORD_OUTER="rodas.sql.keyword_outer";

	public static final String KEYWORD_UPDATE="rodas.sql.keyword_update";

	public static final String KEYWORD_USING="rodas.sql.keyword_using";

	public static final String KEYWORD_WITH="rodas.sql.keyword_with";

	public static final String KEYWORD_NULL="rodas.sql.keyword_null";

	public static final String NAMED_PARAMETER="rodas.sql.named_parameter";

	public static final String UNNAMED_PARAMETER="rodas.sql.unnamed_parameter";

	public static final String OPERATOR_CONCATENATION="rodas.sql.operator_concatenation";

	public static final String OPERATOR_PRIOR="rodas.sql.operator_prior";

	public static final String OPERATOR_CONNECT_BY_ROOT="rodas.sql.operator_connect_by_root";

	public static final String OPERATOR_BETWEEN="rodas.sql.operator_between";

	public static final String OPERATOR_OF="rodas.sql.operator_of";

	public static final String OPERATOR_TYPE="rodas.sql.operator_type";

	public static final String WHITESPACE="rodas.sql.whitespace";

	public static final String PERIOD="rodas.sql.period";

	public static final String TRUE="rodas.sql.true";

	public static final String FALSE="rodas.sql.false";

	public static final String OPERATOR_GREATER_GREATER="rodas.sql.operator_greater_greater";

	public static final String OPERATOR_LOWER_LOWER="rodas.sql.operator_lower_lower";

	private static Set<String> KEYWORDS=createKeywords();

	private static Set<String> createKeywords()
	{
		Set<String> set=new HashSet<>();
		set.add(KEYWORD_SELECT);
		set.add(KEYWORD_FROM);
		set.add(KEYWORD_WHERE);
		set.add(KEYWORD_GROUP);
		set.add(KEYWORD_HAVING);
		set.add(KEYWORD_ORDER);
		set.add(KEYWORD_AS);
		set.add(KEYWORD_BY);
		set.add(KEYWORD_ALL);
		set.add(KEYWORD_DISTINCT);
		set.add(KEYWORD_INNER);
		set.add(KEYWORD_JOIN);
		set.add(KEYWORD_LEFT);
		set.add(KEYWORD_RIGHT);
		set.add(KEYWORD_ON);
		set.add(KEYWORD_OUTER);
		set.add(KEYWORD_USING);
		set.add(KEYWORD_DISTINCTROW);
		set.add(KEYWORD_ASC);
		set.add(KEYWORD_DESC);
		set.add(KEYWORD_FOR);
		set.add(KEYWORD_UPDATE);
		set.add(KEYWORD_WITH);
		return set;
	}

	public static boolean isKeyword(String tokenType)
	{
		return KEYWORDS.contains(tokenType);
	}

	// public SqlToken(String type, String image, String value)
	// {
	// super(type, image, value);
	// // switch (type)
	// // {
	// // case KEYWORD_SELECT:
	// // case KEYWORD_FROM:
	// // case KEYWORD_WHERE:
	// // case KEYWORD_GROUP:
	// // case KEYWORD_HAVING:
	// // case KEYWORD_ORDER:
	// // case KEYWORD_AS:
	// // case KEYWORD_BY:
	// // case KEYWORD_ALL:
	// // case KEYWORD_DISTINCT:
	// // case KEYWORD_INNER:
	// // case KEYWORD_JOIN:
	// // case KEYWORD_LEFT:
	// // case KEYWORD_RIGHT:
	// // case KEYWORD_ON:
	// // case KEYWORD_OUTER:
	// // case KEYWORD_USING:
	// // case KEYWORD_DISTINCTROW:
	// // case KEYWORD_ASC:
	// // case KEYWORD_DESC:
	// // case KEYWORD_FOR:
	// // case KEYWORD_UPDATE:
	// // case KEYWORD_WITH:
	// // this.keyword=true;
	// // break;
	// // default:
	// // this.keyword=false;
	// // }
	// }

}
