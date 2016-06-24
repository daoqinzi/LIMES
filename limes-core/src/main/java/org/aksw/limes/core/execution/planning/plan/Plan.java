package org.aksw.limes.core.execution.planning.plan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the plan of a link specification. A plan consists of a set of
 * instructions that are going to be executed sequentially or in parallel by an
 * execution engine.
 *
 * @author Axel-C. Ngonga Ngomo {@literal <}ngonga {@literal @}
 *         informatik.uni-leipzig.de{@literal >}
 * @author Kleanthi Georgala {@literal <}georgala {@literal @}
 *         informatik.uni-leipzig.de{@literal >}
 * @version 1.0
 */
public class Plan implements IPlan {
    static Logger logger = LoggerFactory.getLogger(Plan.class);
    /**
     * The runtime of the plan.
     */
    protected double runtimeCost;
    /**
     * The size of returned mapping of the plan.
     */
    protected double mappingSize;
    /**
     * The selectivity of the plan.
     */
    protected double selectivity;
    /**
     * The list of instructions of the plan.
     */
    protected List<Instruction> instructionList;

    /**
     * Constructor of Plan class.
     */
    public Plan() {
        instructionList = new ArrayList<Instruction>();
        runtimeCost = 0d;
        mappingSize = 0;
        selectivity = 1d;

    }

    /**
     * Adds an instruction to the instructionList. Each new instruction is added
     * at the end of the list. If an instructions can not be added, a
     * corresponding message is displayed.
     *
     * @param instruction,
     *            the Instruction to add
     */
    @Override
    public void addInstruction(Instruction instruction) {
        if (instruction != null) {
            // instructions are added at the end of the list
            boolean added = instructionList.add(instruction);
            if (!added)
                logger.info("ExecutionPlan.addInstructiun() failed");
        }

    }

    /**
     * Removes the i-th instruction from the instructionList. If the index is
     * lower than 0 or higher than the size of the current list, the removal
     * fails and a corresponding message is displayed.
     *
     * @param i,
     *            Index of instruction to remove
     */
    @Override
    public void removeInstruction(int i) {
        if (i >= getInstructionList().size() || i < 0)
            logger.info("ExecutionPlan.removeInstructiun() failed");
        else
            instructionList.remove(i);
    }

    /**
     * Removes an instruction from a instructionList.
     *
     * @param i
     *            Instruction to remove
     */
    @Override
    public void removeInstruction(Instruction i) {
        instructionList.remove(i);
    }

    /**
     * Returns the list of instructions contained in a instructionList.
     *
     * @return List of instructions
     */
    @Override
    public List<Instruction> getInstructionList() {
        return instructionList;
    }

    /**
     * Sets the instructionList of the plan.
     *
     * @param instructionList,
     *            the instruction list to set
     */
    public void setInstructionList(List<Instruction> instructionList) {
        this.instructionList = instructionList;
    }

    /**
     * Checks whether the instructionList of the current plan is empty.
     *
     * @return true if it is empty or false otherwise
     */
    public boolean isEmpty() {
        return instructionList.isEmpty();
    }

    /**
     * Checks if two plans are equal. Two plans are equal if their instructions
     * lists are the same.
     *
     * @return true if they are equal or false otherwise
     */
    @Override
    public boolean equals(Object other) {
        Plan o = (Plan) other;
        if (o == null)
            return false;
        return (this.instructionList.equals(o.instructionList));

    }

    /**
     * Returns a clone of the current plan. Each non-primitive field of the
     * current plan is cloned by invoking the clone function of the
     * corresponding class.
     *
     * @return clone, the clone of the current plan
     */
    @Override
    public Plan clone() {
        Plan clone = new Plan();

        // clone primitives fields
        clone.setMappingSize(this.mappingSize);
        clone.setRuntimeCost(this.runtimeCost);
        clone.setSelectivity(this.selectivity);

        // clone instructionList
        if (this.instructionList != null) {
            if (this.instructionList.isEmpty() == false) {
                List<Instruction> cloneInstructionList = new ArrayList<Instruction>();
                for (Instruction i : this.instructionList) {
                    cloneInstructionList.add(i.clone());
                }
                clone.setInstructionList(cloneInstructionList);
            } else {
                clone.setInstructionList(new ArrayList<Instruction>());
            }
        } else
            clone.setInstructionList(null);

        return clone;
    }

    /**
     * Returns the size of the current plan. The size of a plan is equal to the
     * size of its instruction list.
     *
     * @return size, the size of the plan
     */
    public int size() {
        return instructionList.size();
    }

    /**
     * Returns the runtime cost of the current plan.
     *
     * @return runtimeCost, the runtime of the current plan
     */
    public double getRuntimeCost() {
        return runtimeCost;
    }

    /**
     * Sets the runtime cost of the plan.
     *
     * @param runtimeCost,
     *            the runtime cost to set
     */
    public void setRuntimeCost(double runtimeCost) {
        this.runtimeCost = runtimeCost;
    }

    /**
     * Returns the mapping size of the plan.
     *
     * @return mappingSize, the mapping size of the current plan
     */
    public double getMappingSize() {
        return mappingSize;
    }

    /**
     * Sets the mapping size of the plan.
     *
     * @param mappingSize,
     *            the mapping size to set
     */
    public void setMappingSize(double mappingSize) {
        this.mappingSize = mappingSize;
    }

    /**
     * Returns the selectivity of the plan.
     *
     * @return selectivity, the selectivity of the current plan
     */
    public double getSelectivity() {
        return selectivity;
    }

    /**
     * Sets the selectivity of the plan.
     *
     * @param selectivity,
     *            the selectivity value to set
     */
    public void setSelectivity(double selectivity) {
        this.selectivity = selectivity;
    }

}
