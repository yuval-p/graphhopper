/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util.shapes;

import com.graphhopper.util.NumHelper;

/**
 * @author Peter Karich
 */
public class OSMGHPoint3D extends GHPoint3D {
    public long osmId;

    public OSMGHPoint3D(double lat, double lon, double elevation, long osmId) {
        super(lat, lon, elevation);
        this.osmId = osmId;
    }


    public long getOsmId() {
        return osmId;
    }

    @Override
    public int hashCode() {
        int hash = 59 * super.hashCode()  + (int) this.osmId ^ (int) this.osmId >>> 32;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        @SuppressWarnings("unchecked")
        final OSMGHPoint3D other = (OSMGHPoint3D) obj;

        return (super.equals(obj)) && NumHelper.equals(osmId, other.osmId);
    }

    @Override
    public String toString() {
        return super.toString() + "," + osmId;
    }

    @Override
    public Double[] toGeoJson() {
        return new Double[]{lon, lat, ele};
    }
}
