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
package com.graphhopper.util;

/**
 * @author Peter Karich
 */
public class FinishInstruction extends Instruction {
    public FinishInstruction(String name, final double lat, final double lon, final double ele, long osmId) {
        super(FINISH, name, new PointList(2, !Double.isNaN(ele), Long.MIN_VALUE != osmId) {
            {
                add(lat, lon, ele, osmId);
            }
        });
    }

    public FinishInstruction(String name, final double lat, final double lon, final double ele) {
        super(FINISH, name, new PointList(2, !Double.isNaN(ele), false) {
            {
                add(lat, lon, ele);
            }
        });
    }
    public FinishInstruction(final double lat, final double lon, final double ele) {
        super(FINISH, "", new PointList(2, !Double.isNaN(ele), false) {
            {
                add(lat, lon, ele, Long.MIN_VALUE);
            }
        });
    }

    public FinishInstruction(final double lat, final double lon, final double ele, long osmId) {
        super(FINISH, "", new PointList(2, !Double.isNaN(ele), Long.MIN_VALUE != osmId) {
            {
                add(lat, lon, ele, osmId);
            }
        });
    }

    public FinishInstruction(String name, PointAccess pointAccess, int node) {
        this(name, pointAccess.getLat(node), pointAccess.getLon(node),
                pointAccess.is3D() ? pointAccess.getEle(node) : Double.NaN, pointAccess.getOsmId(node));
    }

    public FinishInstruction(PointAccess pointAccess, int node) {

        this(pointAccess.getLat(node), pointAccess.getLon(node),
                pointAccess.is3D() ? pointAccess.getEle(node) : Double.NaN, pointAccess.getOsmId(node));
        // this.points.setOsmId(0, osmId);
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public String getTurnDescription(Translation tr) {
        if (rawName)
            return getName();

        return tr.tr("finish");
    }
}
