package com.zql.fileoperationlib.model;

/**
 * A class for represents a user permissions.
 *
 * @see Permission
 */
public class UserPermission extends Permission {

    /**
     * @hide
     */
    public static final char SETUID_E = 's';
    /**
     * @hide
     */
    public static final char SETUID = 'S';
    private static final long serialVersionUID = -900037436267443837L;
    private boolean mSetuid;

    /**
     * Constructor of <code>UserPermission</code>.
     *
     * @param read    If the object can be read
     * @param write   If the object can be written
     * @param execute If the object can be executed
     */
    public UserPermission(boolean read, boolean write, boolean execute) {
        super(read, write, execute);
        this.mSetuid = false;
    }

    /**
     * Constructor of <code>UserPermission</code>.
     *
     * @param read    If the object can be read
     * @param write   If the object can be written
     * @param execute If the object can be executed
     * @param setuid  If the object has the setuid bit active
     */
    public UserPermission(boolean read, boolean write, boolean execute, boolean setuid) {
        super(read, write, execute);
        this.mSetuid = setuid;
    }

    /**
     * Method that returns if the object has the setuid bit active.
     *
     * @return boolean If the object has the setuid bit active.
     */
    public boolean isSetUID() {
        return this.mSetuid;
    }

    /**
     * Method that sets if the object has the setuid bit active.
     *
     * @param setuid If the object has the setuid bit active
     */
    public void setSetUID(boolean setuid) {
        this.mSetuid = setuid;
        invalidateRawString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (this.mSetuid ? 1231 : 1237);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserPermission other = (UserPermission) obj;
        if (this.mSetuid != other.mSetuid) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserPermission [setuid=" + this.mSetuid + //$NON-NLS-1$
                ", permission=" + super.toString() + "]"; //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawString() {
        StringBuilder p = new StringBuilder();
        p.append(isRead() ? READ : UNASIGNED);
        p.append(isWrite() ? WRITE : UNASIGNED);
        if (isSetUID()) {
            p.append(isExecute() ? SETUID_E : SETUID);
        } else {
            p.append(isExecute() ? EXECUTE : UNASIGNED);
        }
        return p.toString();
    }

}
