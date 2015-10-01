using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal
{
    internal static class Extensions
    {
        public static void AddAll<T>(this IList<T> list, IList<T> otherList)
        {
            foreach (var item in otherList) {
                list.Add(item);
            }
        }
    }
}
