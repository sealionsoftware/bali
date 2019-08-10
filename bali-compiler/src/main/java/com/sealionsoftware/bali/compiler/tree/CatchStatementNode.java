package com.sealionsoftware.bali.compiler.tree;

public class CatchStatementNode extends StatementNode {

    private String caughtName;
    private TypeNode caughtType;
    private StatementNode coveredStatement;
    private StatementNode catchStatement;

    public CatchStatementNode(Integer line, Integer character) {
		super(line, character);
	}

    public StatementNode getCoveredStatement() {
        return coveredStatement;
    }

    public void setCoveredStatement(StatementNode covered) {
        children.add(covered);
        this.coveredStatement = covered;
    }

    public void setCatchStatement(StatementNode catchStatement) {
        children.add(catchStatement);
        this.catchStatement = catchStatement;
    }

    public StatementNode getCatchStatement() {
        return catchStatement;
    }

    public String getCaughtName() {
        return caughtName;
    }

    public void setCaughtName(String caughtName) {
        this.caughtName = caughtName;
    }

    public TypeNode getCaughtType() {
        return caughtType;
    }

    public void setCaughtType(TypeNode caughtType) {
        children.add(caughtType);
        this.caughtType = caughtType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
