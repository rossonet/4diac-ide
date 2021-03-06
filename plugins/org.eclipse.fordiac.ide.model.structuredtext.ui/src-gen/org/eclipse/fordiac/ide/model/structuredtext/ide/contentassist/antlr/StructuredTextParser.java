/*
 * generated by Xtext 2.20.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.ide.contentassist.antlr;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import org.eclipse.fordiac.ide.model.structuredtext.ide.contentassist.antlr.internal.InternalStructuredTextParser;
import org.eclipse.fordiac.ide.model.structuredtext.services.StructuredTextGrammarAccess;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;

public class StructuredTextParser extends AbstractContentAssistParser {

	@Singleton
	public static final class NameMappings {

		private final Map<AbstractElement, String> mappings;

		@Inject
		public NameMappings(StructuredTextGrammarAccess grammarAccess) {
			ImmutableMap.Builder<AbstractElement, String> builder = ImmutableMap.builder();
			init(builder, grammarAccess);
			this.mappings = builder.build();
		}

		public String getRuleName(AbstractElement element) {
			return mappings.get(element);
		}

		private static void init(ImmutableMap.Builder<AbstractElement, String> builder,
				StructuredTextGrammarAccess grammarAccess) {
			builder.put(grammarAccess.getVar_Decl_InitAccess().getAlternatives(), "rule__Var_Decl_Init__Alternatives");
			builder.put(grammarAccess.getStmtAccess().getAlternatives(), "rule__Stmt__Alternatives");
			builder.put(grammarAccess.getSubprog_Ctrl_StmtAccess().getAlternatives(),
					"rule__Subprog_Ctrl_Stmt__Alternatives");
			builder.put(grammarAccess.getSelection_StmtAccess().getAlternatives(),
					"rule__Selection_Stmt__Alternatives");
			builder.put(grammarAccess.getIteration_StmtAccess().getAlternatives(),
					"rule__Iteration_Stmt__Alternatives");
			builder.put(grammarAccess.getUnary_ExprAccess().getAlternatives(), "rule__Unary_Expr__Alternatives");
			builder.put(grammarAccess.getPrimary_ExprAccess().getAlternatives(), "rule__Primary_Expr__Alternatives");
			builder.put(grammarAccess.getFunc_CallAccess().getFuncAlternatives_0_0(),
					"rule__Func_Call__FuncAlternatives_0_0");
			builder.put(grammarAccess.getParam_AssignAccess().getAlternatives(), "rule__Param_Assign__Alternatives");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getAlternatives_0(),
					"rule__Variable_Subscript__Alternatives_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getAlternatives(),
					"rule__Multibit_Part_Access__Alternatives");
			builder.put(grammarAccess.getAdapter_NameAccess().getAlternatives(), "rule__Adapter_Name__Alternatives");
			builder.put(grammarAccess.getVariable_NameAccess().getAlternatives(), "rule__Variable_Name__Alternatives");
			builder.put(grammarAccess.getConstantAccess().getAlternatives(), "rule__Constant__Alternatives");
			builder.put(grammarAccess.getNumeric_LiteralAccess().getAlternatives(),
					"rule__Numeric_Literal__Alternatives");
			builder.put(grammarAccess.getInt_LiteralAccess().getValueAlternatives_1_0(),
					"rule__Int_Literal__ValueAlternatives_1_0");
			builder.put(grammarAccess.getSigned_IntAccess().getAlternatives_0(), "rule__Signed_Int__Alternatives_0");
			builder.put(grammarAccess.getBool_ValueAccess().getAlternatives(), "rule__Bool_Value__Alternatives");
			builder.put(grammarAccess.getChar_LiteralAccess().getValueAlternatives_1_0(),
					"rule__Char_Literal__ValueAlternatives_1_0");
			builder.put(grammarAccess.getTime_LiteralAccess().getAlternatives(), "rule__Time_Literal__Alternatives");
			builder.put(grammarAccess.getDurationAccess().getAlternatives_2(), "rule__Duration__Alternatives_2");
			builder.put(grammarAccess.getType_NameAccess().getAlternatives(), "rule__Type_Name__Alternatives");
			builder.put(grammarAccess.getAnd_OperatorAccess().getAlternatives(), "rule__And_Operator__Alternatives");
			builder.put(grammarAccess.getCompare_OperatorAccess().getAlternatives(),
					"rule__Compare_Operator__Alternatives");
			builder.put(grammarAccess.getEqu_OperatorAccess().getAlternatives(), "rule__Equ_Operator__Alternatives");
			builder.put(grammarAccess.getAdd_OperatorAccess().getAlternatives(), "rule__Add_Operator__Alternatives");
			builder.put(grammarAccess.getTerm_OperatorAccess().getAlternatives(), "rule__Term_Operator__Alternatives");
			builder.put(grammarAccess.getUnary_OperatorAccess().getAlternatives(),
					"rule__Unary_Operator__Alternatives");
			builder.put(grammarAccess.getDuration_UnitAccess().getAlternatives(), "rule__Duration_Unit__Alternatives");
			builder.put(grammarAccess.getInt_Type_NameAccess().getAlternatives(), "rule__Int_Type_Name__Alternatives");
			builder.put(grammarAccess.getReal_Type_NameAccess().getAlternatives(),
					"rule__Real_Type_Name__Alternatives");
			builder.put(grammarAccess.getString_Type_NameAccess().getAlternatives(),
					"rule__String_Type_Name__Alternatives");
			builder.put(grammarAccess.getTime_Type_NameAccess().getAlternatives(),
					"rule__Time_Type_Name__Alternatives");
			builder.put(grammarAccess.getTod_Type_NameAccess().getAlternatives(), "rule__Tod_Type_Name__Alternatives");
			builder.put(grammarAccess.getDate_Type_NameAccess().getAlternatives(),
					"rule__Date_Type_Name__Alternatives");
			builder.put(grammarAccess.getDT_Type_NameAccess().getAlternatives(), "rule__DT_Type_Name__Alternatives");
			builder.put(grammarAccess.getAny_Bit_Type_NameAccess().getAlternatives(),
					"rule__Any_Bit_Type_Name__Alternatives");
			builder.put(grammarAccess.getStructuredTextAlgorithmAccess().getGroup(),
					"rule__StructuredTextAlgorithm__Group__0");
			builder.put(grammarAccess.getStructuredTextAlgorithmAccess().getGroup_1(),
					"rule__StructuredTextAlgorithm__Group_1__0");
			builder.put(grammarAccess.getStructuredTextAlgorithmAccess().getGroup_1_1(),
					"rule__StructuredTextAlgorithm__Group_1_1__0");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getGroup(), "rule__Var_Decl_Local__Group__0");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getGroup_5(), "rule__Var_Decl_Local__Group_5__0");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getGroup_6(), "rule__Var_Decl_Local__Group_6__0");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getGroup(), "rule__Var_Decl_Located__Group__0");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getGroup_2(), "rule__Var_Decl_Located__Group_2__0");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getGroup_5(), "rule__Var_Decl_Located__Group_5__0");
			builder.put(grammarAccess.getStmt_ListAccess().getGroup(), "rule__Stmt_List__Group__0");
			builder.put(grammarAccess.getStmt_ListAccess().getGroup_1(), "rule__Stmt_List__Group_1__0");
			builder.put(grammarAccess.getAssign_StmtAccess().getGroup(), "rule__Assign_Stmt__Group__0");
			builder.put(grammarAccess.getSubprog_Ctrl_StmtAccess().getGroup_1(), "rule__Subprog_Ctrl_Stmt__Group_1__0");
			builder.put(grammarAccess.getSubprog_Ctrl_StmtAccess().getGroup_2(), "rule__Subprog_Ctrl_Stmt__Group_2__0");
			builder.put(grammarAccess.getIF_StmtAccess().getGroup(), "rule__IF_Stmt__Group__0");
			builder.put(grammarAccess.getELSIF_ClauseAccess().getGroup(), "rule__ELSIF_Clause__Group__0");
			builder.put(grammarAccess.getELSE_ClauseAccess().getGroup(), "rule__ELSE_Clause__Group__0");
			builder.put(grammarAccess.getCase_StmtAccess().getGroup(), "rule__Case_Stmt__Group__0");
			builder.put(grammarAccess.getCase_SelectionAccess().getGroup(), "rule__Case_Selection__Group__0");
			builder.put(grammarAccess.getCase_SelectionAccess().getGroup_1(), "rule__Case_Selection__Group_1__0");
			builder.put(grammarAccess.getIteration_StmtAccess().getGroup_3(), "rule__Iteration_Stmt__Group_3__0");
			builder.put(grammarAccess.getIteration_StmtAccess().getGroup_4(), "rule__Iteration_Stmt__Group_4__0");
			builder.put(grammarAccess.getFor_StmtAccess().getGroup(), "rule__For_Stmt__Group__0");
			builder.put(grammarAccess.getFor_StmtAccess().getGroup_6(), "rule__For_Stmt__Group_6__0");
			builder.put(grammarAccess.getWhile_StmtAccess().getGroup(), "rule__While_Stmt__Group__0");
			builder.put(grammarAccess.getRepeat_StmtAccess().getGroup(), "rule__Repeat_Stmt__Group__0");
			builder.put(grammarAccess.getOr_ExpressionAccess().getGroup(), "rule__Or_Expression__Group__0");
			builder.put(grammarAccess.getOr_ExpressionAccess().getGroup_1(), "rule__Or_Expression__Group_1__0");
			builder.put(grammarAccess.getXor_ExprAccess().getGroup(), "rule__Xor_Expr__Group__0");
			builder.put(grammarAccess.getXor_ExprAccess().getGroup_1(), "rule__Xor_Expr__Group_1__0");
			builder.put(grammarAccess.getAnd_ExprAccess().getGroup(), "rule__And_Expr__Group__0");
			builder.put(grammarAccess.getAnd_ExprAccess().getGroup_1(), "rule__And_Expr__Group_1__0");
			builder.put(grammarAccess.getCompare_ExprAccess().getGroup(), "rule__Compare_Expr__Group__0");
			builder.put(grammarAccess.getCompare_ExprAccess().getGroup_1(), "rule__Compare_Expr__Group_1__0");
			builder.put(grammarAccess.getEqu_ExprAccess().getGroup(), "rule__Equ_Expr__Group__0");
			builder.put(grammarAccess.getEqu_ExprAccess().getGroup_1(), "rule__Equ_Expr__Group_1__0");
			builder.put(grammarAccess.getAdd_ExprAccess().getGroup(), "rule__Add_Expr__Group__0");
			builder.put(grammarAccess.getAdd_ExprAccess().getGroup_1(), "rule__Add_Expr__Group_1__0");
			builder.put(grammarAccess.getTermAccess().getGroup(), "rule__Term__Group__0");
			builder.put(grammarAccess.getTermAccess().getGroup_1(), "rule__Term__Group_1__0");
			builder.put(grammarAccess.getPower_ExprAccess().getGroup(), "rule__Power_Expr__Group__0");
			builder.put(grammarAccess.getPower_ExprAccess().getGroup_1(), "rule__Power_Expr__Group_1__0");
			builder.put(grammarAccess.getUnary_ExprAccess().getGroup_0(), "rule__Unary_Expr__Group_0__0");
			builder.put(grammarAccess.getPrimary_ExprAccess().getGroup_2(), "rule__Primary_Expr__Group_2__0");
			builder.put(grammarAccess.getFunc_CallAccess().getGroup(), "rule__Func_Call__Group__0");
			builder.put(grammarAccess.getFunc_CallAccess().getGroup_2(), "rule__Func_Call__Group_2__0");
			builder.put(grammarAccess.getFunc_CallAccess().getGroup_2_1(), "rule__Func_Call__Group_2_1__0");
			builder.put(grammarAccess.getParam_Assign_InAccess().getGroup(), "rule__Param_Assign_In__Group__0");
			builder.put(grammarAccess.getParam_Assign_InAccess().getGroup_0(), "rule__Param_Assign_In__Group_0__0");
			builder.put(grammarAccess.getParam_Assign_OutAccess().getGroup(), "rule__Param_Assign_Out__Group__0");
			builder.put(grammarAccess.getVariableAccess().getGroup(), "rule__Variable__Group__0");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getGroup(), "rule__Variable_Subscript__Group__0");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getGroup_1(),
					"rule__Variable_Subscript__Group_1__0");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getGroup_1_3(),
					"rule__Variable_Subscript__Group_1_3__0");
			builder.put(grammarAccess.getVariable_AdapterAccess().getGroup(), "rule__Variable_Adapter__Group__0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getGroup_0(),
					"rule__Multibit_Part_Access__Group_0__0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getGroup_1(),
					"rule__Multibit_Part_Access__Group_1__0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getGroup_2(),
					"rule__Multibit_Part_Access__Group_2__0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getGroup_3(),
					"rule__Multibit_Part_Access__Group_3__0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getGroup_4(),
					"rule__Multibit_Part_Access__Group_4__0");
			builder.put(grammarAccess.getInt_LiteralAccess().getGroup(), "rule__Int_Literal__Group__0");
			builder.put(grammarAccess.getInt_LiteralAccess().getGroup_0(), "rule__Int_Literal__Group_0__0");
			builder.put(grammarAccess.getSigned_IntAccess().getGroup(), "rule__Signed_Int__Group__0");
			builder.put(grammarAccess.getReal_LiteralAccess().getGroup(), "rule__Real_Literal__Group__0");
			builder.put(grammarAccess.getReal_LiteralAccess().getGroup_0(), "rule__Real_Literal__Group_0__0");
			builder.put(grammarAccess.getReal_ValueAccess().getGroup(), "rule__Real_Value__Group__0");
			builder.put(grammarAccess.getReal_ValueAccess().getGroup_3(), "rule__Real_Value__Group_3__0");
			builder.put(grammarAccess.getBool_LiteralAccess().getGroup(), "rule__Bool_Literal__Group__0");
			builder.put(grammarAccess.getBool_LiteralAccess().getGroup_0(), "rule__Bool_Literal__Group_0__0");
			builder.put(grammarAccess.getChar_LiteralAccess().getGroup(), "rule__Char_Literal__Group__0");
			builder.put(grammarAccess.getChar_LiteralAccess().getGroup_0(), "rule__Char_Literal__Group_0__0");
			builder.put(grammarAccess.getDurationAccess().getGroup(), "rule__Duration__Group__0");
			builder.put(grammarAccess.getDurationAccess().getGroup_4(), "rule__Duration__Group_4__0");
			builder.put(grammarAccess.getDuration_ValueAccess().getGroup(), "rule__Duration_Value__Group__0");
			builder.put(grammarAccess.getFix_PointAccess().getGroup(), "rule__Fix_Point__Group__0");
			builder.put(grammarAccess.getFix_PointAccess().getGroup_1(), "rule__Fix_Point__Group_1__0");
			builder.put(grammarAccess.getTime_Of_DayAccess().getGroup(), "rule__Time_Of_Day__Group__0");
			builder.put(grammarAccess.getDaytimeAccess().getGroup(), "rule__Daytime__Group__0");
			builder.put(grammarAccess.getDateAccess().getGroup(), "rule__Date__Group__0");
			builder.put(grammarAccess.getDate_LiteralAccess().getGroup(), "rule__Date_Literal__Group__0");
			builder.put(grammarAccess.getDate_And_TimeAccess().getGroup(), "rule__Date_And_Time__Group__0");
			builder.put(grammarAccess.getDate_And_Time_ValueAccess().getGroup(), "rule__Date_And_Time_Value__Group__0");
			builder.put(grammarAccess.getStructuredTextAlgorithmAccess().getLocalVariablesAssignment_1_1_0(),
					"rule__StructuredTextAlgorithm__LocalVariablesAssignment_1_1_0");
			builder.put(grammarAccess.getStructuredTextAlgorithmAccess().getStatementsAssignment_2(),
					"rule__StructuredTextAlgorithm__StatementsAssignment_2");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getConstantAssignment_1(),
					"rule__Var_Decl_Local__ConstantAssignment_1");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getNameAssignment_2(),
					"rule__Var_Decl_Local__NameAssignment_2");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getTypeAssignment_4(),
					"rule__Var_Decl_Local__TypeAssignment_4");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getArrayAssignment_5_0(),
					"rule__Var_Decl_Local__ArrayAssignment_5_0");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getArraySizeAssignment_5_1(),
					"rule__Var_Decl_Local__ArraySizeAssignment_5_1");
			builder.put(grammarAccess.getVar_Decl_LocalAccess().getInitialValueAssignment_6_1(),
					"rule__Var_Decl_Local__InitialValueAssignment_6_1");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getNameAssignment_1(),
					"rule__Var_Decl_Located__NameAssignment_1");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getLocationAssignment_2_1(),
					"rule__Var_Decl_Located__LocationAssignment_2_1");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getTypeAssignment_4(),
					"rule__Var_Decl_Located__TypeAssignment_4");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getArrayAssignment_5_0(),
					"rule__Var_Decl_Located__ArrayAssignment_5_0");
			builder.put(grammarAccess.getVar_Decl_LocatedAccess().getArraySizeAssignment_5_1(),
					"rule__Var_Decl_Located__ArraySizeAssignment_5_1");
			builder.put(grammarAccess.getStmt_ListAccess().getStatementsAssignment_1_0(),
					"rule__Stmt_List__StatementsAssignment_1_0");
			builder.put(grammarAccess.getAssign_StmtAccess().getVariableAssignment_0(),
					"rule__Assign_Stmt__VariableAssignment_0");
			builder.put(grammarAccess.getAssign_StmtAccess().getExpressionAssignment_2(),
					"rule__Assign_Stmt__ExpressionAssignment_2");
			builder.put(grammarAccess.getIF_StmtAccess().getExpressionAssignment_1(),
					"rule__IF_Stmt__ExpressionAssignment_1");
			builder.put(grammarAccess.getIF_StmtAccess().getStatmentsAssignment_3(),
					"rule__IF_Stmt__StatmentsAssignment_3");
			builder.put(grammarAccess.getIF_StmtAccess().getElseifAssignment_4(), "rule__IF_Stmt__ElseifAssignment_4");
			builder.put(grammarAccess.getIF_StmtAccess().getElseAssignment_5(), "rule__IF_Stmt__ElseAssignment_5");
			builder.put(grammarAccess.getELSIF_ClauseAccess().getExpressionAssignment_1(),
					"rule__ELSIF_Clause__ExpressionAssignment_1");
			builder.put(grammarAccess.getELSIF_ClauseAccess().getStatementsAssignment_3(),
					"rule__ELSIF_Clause__StatementsAssignment_3");
			builder.put(grammarAccess.getELSE_ClauseAccess().getStatementsAssignment_1(),
					"rule__ELSE_Clause__StatementsAssignment_1");
			builder.put(grammarAccess.getCase_StmtAccess().getExpressionAssignment_1(),
					"rule__Case_Stmt__ExpressionAssignment_1");
			builder.put(grammarAccess.getCase_StmtAccess().getCaseAssignment_3(), "rule__Case_Stmt__CaseAssignment_3");
			builder.put(grammarAccess.getCase_StmtAccess().getElseAssignment_4(), "rule__Case_Stmt__ElseAssignment_4");
			builder.put(grammarAccess.getCase_SelectionAccess().getCaseAssignment_0(),
					"rule__Case_Selection__CaseAssignment_0");
			builder.put(grammarAccess.getCase_SelectionAccess().getCaseAssignment_1_1(),
					"rule__Case_Selection__CaseAssignment_1_1");
			builder.put(grammarAccess.getCase_SelectionAccess().getStatementsAssignment_3(),
					"rule__Case_Selection__StatementsAssignment_3");
			builder.put(grammarAccess.getFor_StmtAccess().getVariableAssignment_1(),
					"rule__For_Stmt__VariableAssignment_1");
			builder.put(grammarAccess.getFor_StmtAccess().getFromAssignment_3(), "rule__For_Stmt__FromAssignment_3");
			builder.put(grammarAccess.getFor_StmtAccess().getToAssignment_5(), "rule__For_Stmt__ToAssignment_5");
			builder.put(grammarAccess.getFor_StmtAccess().getByAssignment_6_1(), "rule__For_Stmt__ByAssignment_6_1");
			builder.put(grammarAccess.getFor_StmtAccess().getStatementsAssignment_8(),
					"rule__For_Stmt__StatementsAssignment_8");
			builder.put(grammarAccess.getWhile_StmtAccess().getExpressionAssignment_1(),
					"rule__While_Stmt__ExpressionAssignment_1");
			builder.put(grammarAccess.getWhile_StmtAccess().getStatementsAssignment_3(),
					"rule__While_Stmt__StatementsAssignment_3");
			builder.put(grammarAccess.getRepeat_StmtAccess().getStatementsAssignment_1(),
					"rule__Repeat_Stmt__StatementsAssignment_1");
			builder.put(grammarAccess.getRepeat_StmtAccess().getExpressionAssignment_3(),
					"rule__Repeat_Stmt__ExpressionAssignment_3");
			builder.put(grammarAccess.getOr_ExpressionAccess().getOperatorAssignment_1_1(),
					"rule__Or_Expression__OperatorAssignment_1_1");
			builder.put(grammarAccess.getOr_ExpressionAccess().getRightAssignment_1_2(),
					"rule__Or_Expression__RightAssignment_1_2");
			builder.put(grammarAccess.getXor_ExprAccess().getOperatorAssignment_1_1(),
					"rule__Xor_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getXor_ExprAccess().getRightAssignment_1_2(),
					"rule__Xor_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getAnd_ExprAccess().getOperatorAssignment_1_1(),
					"rule__And_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getAnd_ExprAccess().getRightAssignment_1_2(),
					"rule__And_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getCompare_ExprAccess().getOperatorAssignment_1_1(),
					"rule__Compare_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getCompare_ExprAccess().getRightAssignment_1_2(),
					"rule__Compare_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getEqu_ExprAccess().getOperatorAssignment_1_1(),
					"rule__Equ_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getEqu_ExprAccess().getRightAssignment_1_2(),
					"rule__Equ_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getAdd_ExprAccess().getOperatorAssignment_1_1(),
					"rule__Add_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getAdd_ExprAccess().getRightAssignment_1_2(),
					"rule__Add_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getTermAccess().getOperatorAssignment_1_1(),
					"rule__Term__OperatorAssignment_1_1");
			builder.put(grammarAccess.getTermAccess().getRightAssignment_1_2(), "rule__Term__RightAssignment_1_2");
			builder.put(grammarAccess.getPower_ExprAccess().getOperatorAssignment_1_1(),
					"rule__Power_Expr__OperatorAssignment_1_1");
			builder.put(grammarAccess.getPower_ExprAccess().getRightAssignment_1_2(),
					"rule__Power_Expr__RightAssignment_1_2");
			builder.put(grammarAccess.getUnary_ExprAccess().getOperatorAssignment_0_1(),
					"rule__Unary_Expr__OperatorAssignment_0_1");
			builder.put(grammarAccess.getUnary_ExprAccess().getExpressionAssignment_0_2(),
					"rule__Unary_Expr__ExpressionAssignment_0_2");
			builder.put(grammarAccess.getFunc_CallAccess().getFuncAssignment_0(), "rule__Func_Call__FuncAssignment_0");
			builder.put(grammarAccess.getFunc_CallAccess().getArgsAssignment_2_0(),
					"rule__Func_Call__ArgsAssignment_2_0");
			builder.put(grammarAccess.getFunc_CallAccess().getArgsAssignment_2_1_1(),
					"rule__Func_Call__ArgsAssignment_2_1_1");
			builder.put(grammarAccess.getParam_Assign_InAccess().getVarAssignment_0_0(),
					"rule__Param_Assign_In__VarAssignment_0_0");
			builder.put(grammarAccess.getParam_Assign_InAccess().getExprAssignment_1(),
					"rule__Param_Assign_In__ExprAssignment_1");
			builder.put(grammarAccess.getParam_Assign_OutAccess().getNotAssignment_0(),
					"rule__Param_Assign_Out__NotAssignment_0");
			builder.put(grammarAccess.getParam_Assign_OutAccess().getVarAssignment_1(),
					"rule__Param_Assign_Out__VarAssignment_1");
			builder.put(grammarAccess.getParam_Assign_OutAccess().getExprAssignment_3(),
					"rule__Param_Assign_Out__ExprAssignment_3");
			builder.put(grammarAccess.getVariableAccess().getPartAssignment_1(), "rule__Variable__PartAssignment_1");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getIndexAssignment_1_2(),
					"rule__Variable_Subscript__IndexAssignment_1_2");
			builder.put(grammarAccess.getVariable_SubscriptAccess().getIndexAssignment_1_3_1(),
					"rule__Variable_Subscript__IndexAssignment_1_3_1");
			builder.put(grammarAccess.getVariable_AdapterAccess().getAdapterAssignment_1(),
					"rule__Variable_Adapter__AdapterAssignment_1");
			builder.put(grammarAccess.getVariable_AdapterAccess().getVarAssignment_3(),
					"rule__Variable_Adapter__VarAssignment_3");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getDwordaccessAssignment_0_0(),
					"rule__Multibit_Part_Access__DwordaccessAssignment_0_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getIndexAssignment_0_1(),
					"rule__Multibit_Part_Access__IndexAssignment_0_1");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getWordaccessAssignment_1_0(),
					"rule__Multibit_Part_Access__WordaccessAssignment_1_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getIndexAssignment_1_1(),
					"rule__Multibit_Part_Access__IndexAssignment_1_1");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getByteaccessAssignment_2_0(),
					"rule__Multibit_Part_Access__ByteaccessAssignment_2_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getIndexAssignment_2_1(),
					"rule__Multibit_Part_Access__IndexAssignment_2_1");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getBitaccessAssignment_3_0(),
					"rule__Multibit_Part_Access__BitaccessAssignment_3_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getIndexAssignment_3_1(),
					"rule__Multibit_Part_Access__IndexAssignment_3_1");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getBitaccessAssignment_4_0(),
					"rule__Multibit_Part_Access__BitaccessAssignment_4_0");
			builder.put(grammarAccess.getMultibit_Part_AccessAccess().getIndexAssignment_4_1(),
					"rule__Multibit_Part_Access__IndexAssignment_4_1");
			builder.put(grammarAccess.getVariable_PrimaryAccess().getVarAssignment(),
					"rule__Variable_Primary__VarAssignment");
			builder.put(grammarAccess.getInt_LiteralAccess().getTypeAssignment_0_0(),
					"rule__Int_Literal__TypeAssignment_0_0");
			builder.put(grammarAccess.getInt_LiteralAccess().getValueAssignment_1(),
					"rule__Int_Literal__ValueAssignment_1");
			builder.put(grammarAccess.getReal_LiteralAccess().getTypeAssignment_0_0(),
					"rule__Real_Literal__TypeAssignment_0_0");
			builder.put(grammarAccess.getReal_LiteralAccess().getValueAssignment_1(),
					"rule__Real_Literal__ValueAssignment_1");
			builder.put(grammarAccess.getBool_LiteralAccess().getTypeAssignment_0_0(),
					"rule__Bool_Literal__TypeAssignment_0_0");
			builder.put(grammarAccess.getBool_LiteralAccess().getValueAssignment_1(),
					"rule__Bool_Literal__ValueAssignment_1");
			builder.put(grammarAccess.getChar_LiteralAccess().getTypeAssignment_0_0(),
					"rule__Char_Literal__TypeAssignment_0_0");
			builder.put(grammarAccess.getChar_LiteralAccess().getLengthAssignment_0_1(),
					"rule__Char_Literal__LengthAssignment_0_1");
			builder.put(grammarAccess.getChar_LiteralAccess().getValueAssignment_1(),
					"rule__Char_Literal__ValueAssignment_1");
			builder.put(grammarAccess.getDurationAccess().getTypeAssignment_0(), "rule__Duration__TypeAssignment_0");
			builder.put(grammarAccess.getDurationAccess().getNegativeAssignment_2_1(),
					"rule__Duration__NegativeAssignment_2_1");
			builder.put(grammarAccess.getDurationAccess().getValueAssignment_3(), "rule__Duration__ValueAssignment_3");
			builder.put(grammarAccess.getDurationAccess().getValueAssignment_4_1(),
					"rule__Duration__ValueAssignment_4_1");
			builder.put(grammarAccess.getDuration_ValueAccess().getValueAssignment_0(),
					"rule__Duration_Value__ValueAssignment_0");
			builder.put(grammarAccess.getDuration_ValueAccess().getUnitAssignment_1(),
					"rule__Duration_Value__UnitAssignment_1");
			builder.put(grammarAccess.getTime_Of_DayAccess().getTypeAssignment_0(),
					"rule__Time_Of_Day__TypeAssignment_0");
			builder.put(grammarAccess.getTime_Of_DayAccess().getValueAssignment_2(),
					"rule__Time_Of_Day__ValueAssignment_2");
			builder.put(grammarAccess.getDateAccess().getTypeAssignment_0(), "rule__Date__TypeAssignment_0");
			builder.put(grammarAccess.getDateAccess().getValueAssignment_2(), "rule__Date__ValueAssignment_2");
			builder.put(grammarAccess.getDate_And_TimeAccess().getTypeAssignment_0(),
					"rule__Date_And_Time__TypeAssignment_0");
			builder.put(grammarAccess.getDate_And_TimeAccess().getValueAssignment_2(),
					"rule__Date_And_Time__ValueAssignment_2");
		}
	}

	@Inject
	private NameMappings nameMappings;

	@Inject
	private StructuredTextGrammarAccess grammarAccess;

	@Override
	protected InternalStructuredTextParser createParser() {
		InternalStructuredTextParser result = new InternalStructuredTextParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		return nameMappings.getRuleName(element);
	}

	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public StructuredTextGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(StructuredTextGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}

	public NameMappings getNameMappings() {
		return nameMappings;
	}

	public void setNameMappings(NameMappings nameMappings) {
		this.nameMappings = nameMappings;
	}
}
