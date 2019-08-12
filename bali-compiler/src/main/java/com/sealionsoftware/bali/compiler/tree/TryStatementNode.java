package com.sealionsoftware.bali.compiler.tree;

import java.util.UUID;

public class TryStatementNode extends StatementNode {

    private String caughtName;
    private TypeNode caughtType;
    private StatementNode coveredStatement;
    private StatementNode catchBlock;
    private UUID id = UUID.randomUUID();

    public TryStatementNode(Integer line, Integer character) {
		super(line, character);
	}

    public StatementNode getCoveredStatement() {
        return coveredStatement;
    }

    public void setCoveredStatement(StatementNode covered) {
        children.add(covered);
        this.coveredStatement = covered;
    }

    public void setCatchBlock(StatementNode catchBlock) {
        children.add(catchBlock);
        this.catchBlock = catchBlock;
    }

    public StatementNode getCatchBlock() {
        return catchBlock;
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


    public UUID getId() {
        return id;
    }
}
