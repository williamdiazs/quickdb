package cat.quickdb.db.dbms.postgre;

import cat.quickdb.db.dbms.mysql.ColumnDefined;

public class ColumnDefinedPostgre extends ColumnDefined {

    public ColumnDefinedPostgre() {
    }

    public ColumnDefinedPostgre(String name, String type, int length,
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

        if (this.isAutoIncrement()) {
            this.setType("serial");
        }

        column.append(this.getName() + " " + this.getType() + " ");
        if (this.getLength() != -1) {
            column.append("(" + this.getLength() + ") ");
        } else if (this.getLength() == -1 &&
                this.type.equalsIgnoreCase("character varying")) {
            column.append("(150) ");
        }

        if (this.isNotNull()) {
            column.append("NOT NULL ");
        } else {
            column.append("NULL ");
        }

        if (this.defaultValue.length() != 0) {
            column.append("DEFAULT '" + this.getDefaultValue() + "' ");
        }

        return column.toString();
    }

    @Override
    public boolean hasConstraints() {
        return (this.isUnique() || this.isPrimary());
    }

    @Override
    public String obtainConstraints() {
        StringBuilder constraint = new StringBuilder();

        if (this.isUnique()) {
            constraint.append("CONSTRAINT un_" + this.getName() + " " +
                    "UNIQUE (" + this.getName() + ") ");
        }

        /*if (this.isPrimary()) {
            if(constraint.length() != 0) constraint.append(", ");
            constraint.append("CONSTRAINT 'pk_" + this.getName() + "' " +
                    "PRIMARY KEY (" + this.getName() + ") ");
        }*/

        return constraint.toString();
    }
}
