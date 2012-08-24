/**
 * File: $HeadURL$
 * Revision: $Rev$
 * Last modified: $Date$
 * Last modified by: $Author$
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Contacting the authors:
 *   Mario Arias:               mario.arias@deri.org
 *   Javier D. Fernandez:       jfergar@infor.uva.es
 *   Miguel A. Martinez-Prieto: migumar2@infor.uva.es
 *   Alejandro Andres:          fuzzy.alej@gmail.com
 */

package org.rdfhdt.hdt.dictionary.impl;

import java.io.IOException;
import java.io.InputStream;

import org.rdfhdt.hdt.dictionary.DictionarySection;
import org.rdfhdt.hdt.dictionary.QueryableDictionarySection;
import org.rdfhdt.hdt.listener.ProgressListener;
import org.rdfhdt.hdt.options.HDTSpecification;

/**
 * @author mario.arias
 *
 */
public class DictionarySectionFactory {
	
	public static QueryableDictionarySection createInstance(InputStream input, ProgressListener listener) throws IOException {
		int dictType = input.read();
		switch(dictType) {
		case HashDictionarySection.TYPE_INDEX:
			return new HashDictionarySection(new HDTSpecification());
		case PFCDictionarySection.TYPE_INDEX:
//			return new DictionarySectionPFC(new HDTSpecification());
			//return new DictionarySectionCache(new DictionarySectionPFC(new HDTSpecification()));
			return new DictionarySectionCache(new PFCDictionarySectionBig(new HDTSpecification()));
		}
		throw new IOException("DictionarySection implementation not available for id "+dictType);
	}
	
	public static DictionarySection loadFrom(InputStream input, ProgressListener listener) throws IOException {
		input.mark(64);
		int dictType = input.read();
		input.reset();
		input.mark(64);		// To allow children to reset() and try another instance.
		
		QueryableDictionarySection section=null;
		
		switch(dictType) {
		case HashDictionarySection.TYPE_INDEX:
			section = new HashDictionarySection(new HDTSpecification());
			section.load(input, listener);
			return section;
		case PFCDictionarySection.TYPE_INDEX:
			try{
				// First try load using the standard PFC 
				section = new PFCDictionarySection(new HDTSpecification());
				section.load(input, listener);
			} catch (IllegalArgumentException e) {
				// The PFC Could not load the file because it is too big, use PFCBig
				section = new PFCDictionarySectionBig(new HDTSpecification());
				section.load(input, listener);
			}
			return new DictionarySectionCache(section);
		}
		throw new IOException("DictionarySection implementation not available for id "+dictType);
	}
}
