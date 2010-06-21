package quickdb.db.dbms.firebird;

/**
 *
 * @author Diego Sarmentero
 */
public class ColumnDefined {

    protected String name;
    protected String type;
    protected int length;
    protected boolean notNull;
    protected String defaultValue;
    protected boolean autoIncrement;
    protected boolean unique;
    protected boolean primary;
    protected String format;
    protected String storage;

    public ColumnDefined() {
    }

    public ColumnDefined(String name, String type, int length,
            boolean notNull, String defaultValue, boolean autoIncrement,
            boolean unique, boolean primary, String format, String storage) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.notNull = notNull;
        this.defaultValue = defaultValue;
        this.autoIncrement = autoIncrement;
        this.unique = unique;
        this.primary = primary;
        this.format = format;
        this.storage = storage;
    }

    @Override
    public String toString() {
        StringBuilder column = new StringBuilder();
        column.append(this.getName() + " " + this.getType() + " ");
        if (this.getLength() != -1) {
            column.append("(" + this.getLength() + ") ");
        } else if (this.getLength() == -1 &&
                this.type.equalsIgnoreCase("varchar")) {
            column.append("(150) ");
        }

        if (this.isNotNull()) {
            column.append("not null ");
        } else {
            column.append("null ");
        }

        if (this.defaultValue.length() != 0) {
            column.append("default '" + this.getDefaultValue() + "' ");
        }

        return column.toString();
    }

    public boolean hasConstraints() {
        return (this.isUnique() || this.isPrimary());
    }

    public String obtainConstraints() {
        StringBuilder constraint = new StringBuilder();

        if (this.isUnique()) {
            constraint.append("UNIQUE (" + this.getName() + ") ");
        }

        return constraint.toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the notNull
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * @param notNull the notNull to set
     */
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the autoIncrement
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * @param autoIncrement the autoIncrement to set
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * @return the primary
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * @param primary the primary to set
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the storage
     */
    public String getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(String storage) {
        this.storage = storage;
    }
}
