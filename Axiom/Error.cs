using System;
using System.Runtime.Serialization;

namespace Axiom
{
    [Serializable]
    public class Error : Exception
    {
        public Error() { }

        public Error(string message) : base(message) { }

        public Error(string message, Exception inner) : base(message, inner) { }

        protected Error(SerializationInfo info, StreamingContext context)
            : base(info, context) { }

// TODO make this internal
        public static void Report(string message, Position position = null)
        {
            if (position == null) {
                throw new Error(message);
            } else {
                var error = new Error(message);
                error.Position = position;

                message = string.Format("{0}: {1}", position, message);

                throw error;
            }
        }

        public Position Position { get; set; }
    }
}
