/*
 * Copyright (c) 2000-2007 JetBrains s.r.o. All Rights Reserved.
 */
package com.intellij.aop.psi;

import javax.annotation.Nonnull;

import com.intellij.aop.lexer.AopLexer;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import consulo.lang.LanguageVersion;

/**
 * @author peter
 */
public class AopPointcutExpressionParserDefinition implements ParserDefinition, AopElementTypes {
  private static final TokenSet WHITE_SPACES = TokenSet.create(AopElementTypes.WHITE_SPACE);

  @Nonnull
  public Lexer createLexer(LanguageVersion languageVersion) {
    return new AopLexer();
  }

  public IFileElementType getFileNodeType() {
    return AOP_POINTCUT_EXPRESSION_FILE;
  }

  @Nonnull
  public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
    return WHITE_SPACES;
  }

  @Nonnull
  public TokenSet getCommentTokens(LanguageVersion languageVersion) {
    return TokenSet.EMPTY;
  }

  @Nonnull
  public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
    return TokenSet.EMPTY;
  }

  @Nonnull
  public PsiParser createParser(LanguageVersion languageVersion) {
    return new AopPrattParser();
  }

  @Nonnull
  public PsiElement createElement(ASTNode node) {
    final IElementType elementType = node.getElementType();
    if (elementType == AOP_ANNOTATION_EXPRESSION) return new AopAnnotationExpression(node);
    if (elementType == AOP_ANNOTATION_HOLDER) return new AopAnnotationHolder(node);
    if (elementType == AOP_ANNOTATION_VALUES) return new AopElementBase(node) {
      @Override
      public String toString() {
        return "AopAnnotationValues";
      }
    };
    if (elementType == AOP_ANNOTATED_TYPE_EXPRESSION) return new AopAnnotatedTypeExpression(node);

    if (elementType == AOP_POINTCUT_REFERENCE) return new PsiPointcutReferenceExpression(node);
    if (elementType == AOP_MEMBER_REFERENCE_EXPRESSION) return new AopMemberReferenceExpression(node); 
    if (elementType == AOP_CONSTRUCTOR_REFERENCE_EXPRESSION) return new AopConstructorReferenceExpression(node);

    if (elementType == AOP_REFERENCE_EXPRESSION) return new AopReferenceExpression(node);
    if (elementType == AOP_REFERENCE_HOLDER) return new AopReferenceHolder(node);
    if (elementType == AOP_ARRAY_EXPRESSION) return new AopArrayExpression(node);
    if (elementType == AOP_GENERIC_TYPE_EXPRESSION) return new AopGenericTypeExpression(node);
    if (elementType == AOP_SUBTYPE_EXPRESSION) return new AopSubtypeExpression(node);
    if (elementType == AOP_WILDCARD_EXPRESSION) return new AopWildcardExpression(node);
    if (elementType == AOP_PRIMITIVE_TYPE_EXPRESSION) return new AopPrimitiveTypeExpression(node);

    if (elementType == AOP_PARAMETER_LIST) return new AopParameterList(node);
    if (elementType == AOP_TYPE_PARAMETER_LIST) return new AopTypeParameterList(node);
    if (elementType == AOP_THROWS_LIST) return new AopThrowsList(node);
    if (elementType == AOP_MODIFIER_LIST) return new AopModifierList(node);

    if (elementType == AOP_BINARY_EXPRESSION || elementType == AOP_POINTCUT_BINARY_EXPRESSION) return new AopBinaryExpression(node);
    if (elementType == AOP_NOT_EXPRESSION || elementType == AOP_POINTCUT_NOT_EXPRESSION) return new AopNotExpression(node);
    if (elementType == AOP_PARENTHESIZED_EXPRESSION || elementType == AOP_POINTCUT_PARENTHESIZED_EXPRESSION) return new AopParenthesizedExpression(node);

    if (elementType instanceof AopPointcutElementType) {
      return ((AopPointcutElementType)elementType).createPsi(node);
    }

    throw new UnsupportedOperationException(elementType.toString());
  }

  public PsiFile createFile(FileViewProvider viewProvider) {
    return new AopPointcutExpressionFile(viewProvider);
  }

  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return ParserDefinition.SpaceRequirements.MAY;
  }

}
