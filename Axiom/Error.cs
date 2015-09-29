using System;
using System.Runtime.Serialization;

namespace Axiom
{
    [Serializable]
    public class Error : Exception
    {
        internal Error() { }

        internal Error(string message) : base(message) { }

        internal Error(string message, Exception inner) : base(message, inner) { }

        protected Error(SerializationInfo info, StreamingContext context)
            : base(info, context)
        { }

        internal static void Report(Position position, string message, params object[] args)
        {
            Report(position, string.Format(message, args));
        }

        internal static void Report(Position position, string message)
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
