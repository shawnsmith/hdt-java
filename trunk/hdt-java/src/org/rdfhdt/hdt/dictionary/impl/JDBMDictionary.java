/**
 * File: $HeadURL: http://hdt-java.googlecode.com/svn/trunk/hdt-java/src/org/rdfhdt/hdt/dictionary/impl/HashDictionary.java $
 * Revision: $Rev: 17 $
 * Last modified: $Date: 2012-07-03 21:43:15 +0100 (Tue, 03 Jul 2012) $
 * Last modified by: $Author: mario.arias@gmail.com $
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jdbm.DB;
import org.apache.jdbm.DBMaker;
import org.rdfhdt.hdt.exceptions.NotImplementedException;
import org.rdfhdt.hdt.listener.ProgressListener;
import org.rdfhdt.hdt.options.ControlInformation;
import org.rdfhdt.hdt.options.HDTSpecification;

/**
 * @author mario.arias
 *
 */
public class JDBMDictionary extends BaseModifiableDictionary {

	private DB db;

	public JDBMDictionary(HDTSpecification spec) {

		super(spec);
		db = setupDB(spec);

		// FIXME: Read stuff from properties
		subjects = new JDBMDictionarySection(spec, db, "subjects");
		predicates = new JDBMDictionarySection(spec, db, "predicates");
		objects = new JDBMDictionarySection(spec, db, "objects");
		shared = new JDBMDictionarySection(spec, db, "shared");
	}

	private DB setupDB(HDTSpecification spec) {

		//FIXME read from specs...
		File folder = new File("DB");
		if (!folder.exists() && !folder.mkdir()){
			throw new RuntimeException("Unable to create DB folder...");
		}

		//FIXME read from specs...
		DBMaker db = DBMaker.openFile("DB/DBfile");
		db.closeOnExit();
		db.deleteFilesAfterClose();
		db.disableTransactions(); //more performance
		db.enableHardCache(); //db.enableMRUCache(); + db.setMRUCacheSize(cacheSize);
		
		return db.make();
	}

	@Override
	public void startProcessing() {
		// do nothing
	}
	
	@Override
	public void endProcessing() {
		// do nothing
	}
	
	@Override
	public void close() throws IOException {
		db.close();
	}

	@Override
	public void save(OutputStream output, ControlInformation ci,
			ProgressListener listener) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public void load(InputStream input, ControlInformation ci,
			ProgressListener listener) throws IOException {
		throw new NotImplementedException();
	}
	
}
