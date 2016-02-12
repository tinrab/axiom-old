using System.Collections.Generic;

namespace Axiom.Internal
{
    internal static class Extensions
    {
        public static void AddAll<T>(this IList<T> list, ICollection<T> otherList)
        {
            foreach (var item in otherList) {
                list.Add(item);
            }
        }
    }
}
