package br.usp.each.opal.dataflow;

import java.util.BitSet;

public class ProgramBlock {
	
	private int id;
	private BitSet defs = new BitSet();
	private BitSet pUses = new BitSet();
	private BitSet cUses = new BitSet();
	private BitSet refs = new BitSet();
	private BitSet undefs = new BitSet();
	
	public ProgramBlock(int id) {
		this.id = id;
	}
	
	public void def(int i) {
		defs.set(i);
	}
	
	public void puse(int i) {
		pUses.set(i);
	}
	
	public void cuse(int i) {
		cUses.set(i);
	}
	
	public void ref(int i) {
		refs.set(i);
	}
	
	public void undef(int i) {
		undefs.set(i);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isDef(int x) {
		return defs.get(x);
	}
	
	public boolean isRef(int x) {
		return refs.get(x);
	}
	
	public boolean isCUse(int x) {
		return cUses.get(x);
	}
	
	public boolean isPUse(int x) {
		return pUses.get(x);
	}
	
	public BitSet getDefs() {
		return defs;
	}

	public BitSet getPUses() {
		return pUses;
	}
	
	public BitSet getCUses() {
		return cUses;
	}

	public BitSet getRefs() {
		return refs;
	}
	
	public BitSet getUndefs() {
		return undefs;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}
}
