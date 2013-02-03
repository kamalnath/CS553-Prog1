/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;


public class Params implements Cloneable {

    protected boolean measureCpuTime = false;
    protected boolean isWriteOP = true;
    protected boolean manyExecutions = true;
     
    protected double warmupTime = 10.0;
    protected double executionTimeGoal = 1;
    protected int numberMeasurements = 600;
    protected double confidenceLevel = 0.95;
    protected long numberActions = 1;
    protected long numberThreads = 1;
    
    private String tempPath;
    private long factor;

    public long getFactor() {
        return factor;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public long getNumberThreads() {
        return numberThreads;
    }

    public void setNumberThreads(long numberThreads) {
        this.numberThreads = numberThreads;
    }

    public boolean isIsWriteOP() {
        return isWriteOP;
    }

    public void setIsWriteOP(boolean isWriteOP) {
        this.isWriteOP = isWriteOP;
    }

    

    public Params() {
    }

    public Params(boolean manyExecutions) {
        setManyExecutions(manyExecutions);
    }

    public Params(long numberActions) throws IllegalArgumentException {
        setNumberActions(numberActions);
    }

    public Params(boolean manyExecutions, long numberActions) throws IllegalArgumentException {
        setManyExecutions(manyExecutions);
        setNumberActions(numberActions);
    }

    public boolean getMeasureCpuTime() {
        return measureCpuTime;
    }

    public void setMeasureCpuTime(boolean measureCpuTime) {
        this.measureCpuTime = measureCpuTime;
    }

    public boolean getManyExecutions() {
        return manyExecutions;
    }

    public void setManyExecutions(boolean manyExecutions) {
        this.manyExecutions = manyExecutions;
    }

    public double getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(double warmupTime) throws IllegalArgumentException {
        this.warmupTime = warmupTime;
    }

    public double getExecutionTimeGoal() {
        return executionTimeGoal;
    }

    public void setExecutionTimeGoal(double executionTimeGoal) throws IllegalArgumentException {
        this.executionTimeGoal = executionTimeGoal;
    }

    public int getNumberMeasurements() {
        return numberMeasurements;
    }

    public void setNumberMeasurements(int numberMeasurements) throws IllegalArgumentException {
        this.numberMeasurements = numberMeasurements;
    }

    public double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(double confidenceLevel) throws IllegalArgumentException {
        if ((confidenceLevel <= 0) || (confidenceLevel >= 1)) {
            throw new IllegalArgumentException("confidenceLevel = " + confidenceLevel + " is an illegal value");
        }

        this.confidenceLevel = confidenceLevel;
    }

    public long getNumberActions() {
        return numberActions;
    }

    public void setNumberActions(long numberActions) throws IllegalArgumentException {

        this.numberActions = numberActions;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();	
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        Params other = (Params) obj;
        return (this.measureCpuTime == other.measureCpuTime)
                && (this.manyExecutions == other.manyExecutions)
                && (this.warmupTime == other.warmupTime)
                && (this.executionTimeGoal == other.executionTimeGoal)
                && (this.numberMeasurements == other.numberMeasurements)
                && (this.confidenceLevel == other.confidenceLevel)
                && (this.numberActions == other.numberActions);

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.measureCpuTime ? 1 : 0);
        hash = 23 * hash + (this.manyExecutions ? 1 : 0);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.warmupTime) ^ (Double.doubleToLongBits(this.warmupTime) >>> 32));
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.executionTimeGoal) ^ (Double.doubleToLongBits(this.executionTimeGoal) >>> 32));
        hash = 23 * hash + this.numberMeasurements;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.confidenceLevel) ^ (Double.doubleToLongBits(this.confidenceLevel) >>> 32));
        hash = 23 * hash + (int) (this.numberActions ^ (this.numberActions >>> 32));
        return hash;
    }

    String getTempPath() {
        return tempPath;
    }

    public void setFactor(long i) {
       this.factor=i;
    }
}