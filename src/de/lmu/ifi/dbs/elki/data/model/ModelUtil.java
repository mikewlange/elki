package de.lmu.ifi.dbs.elki.data.model;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.math.linearalgebra.Vector;
import de.lmu.ifi.dbs.elki.utilities.datastructures.arraylike.ArrayLikeUtil;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2014
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Utility classes for dealing with cluster models.
 * 
 * @author Erich Schubert
 */
public final class ModelUtil {
  /**
   * Get (and convert!) the representative vector for a cluster model.
   * 
   * <b>Only representative-based models are supported!</b>
   * 
   * {@code null} is returned when the model is not supported!
   * 
   * @param model Model
   * @param relation Data relation (for representatives specified per DBID)
   * @param factory Vector factory, for type conversion.
   * @return Vector of type V, {@code null} if not supported.
   * @param <V> desired vector type
   */
  @SuppressWarnings("unchecked")
  public static <V extends NumberVector> V getRepresentative(Model model, Relation<? extends V> relation, NumberVector.Factory<V> factory) {
    // Mean model contains a numeric Vector
    if(model instanceof MeanModel) {
      final Vector p = ((MeanModel) model).getMean();
      if(factory.getRestrictionClass().isInstance(p)) {
        return (V) p;
      }
      return factory.newNumberVector(p.getArrayRef());
    }
    // Handle medoid models
    if(model instanceof MedoidModel) {
      NumberVector p = relation.get(((MedoidModel) model).getMedoid());
      if(factory.getRestrictionClass().isInstance(p)) {
        return (V) p;
      }
      return factory.newNumberVector(p, ArrayLikeUtil.NUMBERVECTORADAPTER);
    }
    return null;
  }

  /**
   * Get (and convert!) the representative vector for a cluster model.
   * 
   * <b>Only representative-based models are supported!</b>
   * 
   * {@code null} is returned when the model is not supported!
   * 
   * @param model Model
   * @param relation Data relation (for representatives specified per DBID)
   * @return Some {@link NumberVector}, {@code null} if not supported.
   */
  public static NumberVector getRepresentative(Model model, Relation<? extends NumberVector> relation) {
    // Mean model contains a numeric Vector
    if(model instanceof MeanModel) {
      return ((MeanModel) model).getMean();
    }
    // Handle medoid models
    if(model instanceof MedoidModel) {
      return relation.get(((MedoidModel) model).getMedoid());
    }
    return null;
  }
}