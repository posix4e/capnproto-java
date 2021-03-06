package org.capnproto;

public class PointerReader {
    public SegmentReader segment;
    public int pointer; // word offset
    public int nestingLimit;

    public PointerReader() {
        this.segment = null;
        this.pointer = 0; // XXX ?
        this.nestingLimit = 0x7fffffff;
    }

    public PointerReader(SegmentReader segment, int pointer, int nestingLimit) {
        this.segment = segment;
        this.pointer = pointer;
        this.nestingLimit = nestingLimit;
    }

    public static PointerReader getRoot(SegmentReader segment,
                                        WordPointer location,
                                        int nestingLimit) {
        // TODO bounds check
        return new PointerReader(segment, location.offset, nestingLimit);
    }

    public boolean isNull() {
        return this.segment.ptr.getLong(this.pointer) == 0;
    }

    public StructReader getStruct() {
        return WireHelpers.readStructPointer(this.segment,
                                             this.pointer,
                                             this.nestingLimit);
    }

    public ListReader getList(byte expectedElementSize) {
        // TODO check nullness
        return WireHelpers.readListPointer(this.segment,
                                           this.pointer,
                                           expectedElementSize,
                                           this.nestingLimit);
    }

    public Text.Reader getText() {
        return WireHelpers.readTextPointer(this.segment,
                                           this.pointer);
    }
}
