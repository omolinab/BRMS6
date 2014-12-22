package org.drools.bug.leak;

import java.io.Serializable;

public class SimpleFactWithExpire implements Serializable {

	private int code;
	private Long dateEvt;

	public SimpleFactWithExpire(final int aCode, final Long dateEvt) {
		this.code = aCode;
		this.dateEvt = dateEvt;
	}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Long getDateEvt() {
        return dateEvt;
    }

    public void setDateEvt(Long dateEvt) {
        this.dateEvt = dateEvt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleFactWithExpire that = (SimpleFactWithExpire) o;

        if (code != that.code) return false;
        if (!dateEvt.equals(that.dateEvt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + dateEvt.hashCode();
        return result;
    }

    @Override
	public String toString() {
		return getClass().getSimpleName() + " (code=" + code + ")";
	}
}
