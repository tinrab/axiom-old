namespace Axiom
{
    public class Position
    {
        public Position()
        {
            LineBegin = ColumnBegin = LineEnd = ColumnEnd = 1;
        }

        public Position(int lineBegin, int columnBegin, int lineEnd, int columnEnd)
        {
            LineBegin = lineBegin;
            ColumnBegin = columnBegin;
            LineEnd = lineEnd;
            ColumnEnd = columnEnd;
        }

        public int LineBegin { get; set; }
        public int ColumnBegin { get; set; }
        public int LineEnd { get; set; }
        public int ColumnEnd { get; set; }

        public override string ToString()
        {
            return string.Format("[{0}, {1}, {2}, {3}]", LineBegin, ColumnBegin, LineEnd, ColumnEnd);
        }
    }
}
