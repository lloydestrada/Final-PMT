"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { getCurrentUser } from "@/lib/auth";

export default function DashboardLayout({ children }) {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState(null);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    if (user) {
      setCurrentUser({
        ...user,
        role: user.role?.toUpperCase(),
      });
    }
  }, []);

  const handleLogout = () => {
    router.push("/"); // Redirect to login page
  };

  return (
    <div className="flex min-h-screen bg-gray-100">
      {/* Mobile Sidebar */}
      <div
        className={`fixed inset-0 z-40 md:hidden ${
          sidebarOpen ? "block" : "hidden"
        }`}
      >
        <div
          className="fixed inset-0 bg-black opacity-50"
          onClick={() => setSidebarOpen(false)}
        ></div>
        <aside className="fixed left-0 top-0 w-64 bg-gray-900 text-white h-full flex flex-col justify-between p-6">
          <div>
            <div className="text-3xl font-bold text-cyan-500 mb-6">PMT</div>
            <nav className="flex flex-col space-y-2">
              <Link
                href="/dashboard"
                className="p-2 rounded hover:bg-gray-700 font-medium"
              >
                Home
              </Link>
              <Link
                href="/dashboard/members"
                className="p-2 rounded hover:bg-gray-700 font-medium"
              >
                Members
              </Link>
              <Link
                href="/dashboard/projects"
                className="p-2 rounded hover:bg-gray-700 font-medium"
              >
                Projects
              </Link>
              <Link
                href="/dashboard/tasks"
                className="p-2 rounded hover:bg-gray-700 font-medium"
              >
                Tasks
              </Link>
              {currentUser &&
                ["ADMIN", "SUPER_ADMIN"].includes(currentUser.role) && (
                  <Link
                    href="/dashboard/change-logs"
                    className="p-2 rounded hover:bg-gray-700 font-medium"
                  >
                    Change Logs
                  </Link>
                )}
            </nav>
          </div>
          <div>
            <button
              onClick={handleLogout}
              className="w-full py-2 bg-cyan-600 text-white rounded-lg hover:bg-cyan-700 transition"
            >
              Logout
            </button>
          </div>
        </aside>
      </div>

      {/* Desktop Sidebar */}
      <aside className="hidden md:flex md:flex-col md:w-64 bg-gray-900 text-white justify-between min-h-screen p-6">
        <div>
          <div className="text-3xl font-bold mb-6 text-cyan-500">PMT</div>
          <nav className="flex flex-col space-y-2">
            <Link
              href="/dashboard"
              className="p-2 rounded hover:bg-gray-700 font-medium"
            >
              Home
            </Link>
            <Link
              href="/dashboard/members"
              className="p-2 rounded hover:bg-gray-700 font-medium"
            >
              Members
            </Link>
            <Link
              href="/dashboard/projects"
              className="p-2 rounded hover:bg-gray-700 font-medium"
            >
              Projects
            </Link>
            <Link
              href="/dashboard/tasks"
              className="p-2 rounded hover:bg-gray-700 font-medium"
            >
              Tasks
            </Link>
            {currentUser &&
              ["ADMIN", "SUPER_ADMIN"].includes(currentUser.role) && (
                <Link
                  href="/dashboard/change-logs"
                  className="p-2 rounded hover:bg-gray-700 font-medium"
                >
                  Change Logs
                </Link>
              )}
          </nav>
        </div>
        <div>
          <button
            onClick={handleLogout}
            className="w-full py-2 bg-cyan-600 text-white rounded-lg hover:bg-cyan-700 transition"
          >
            Logout
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-auto">
        {/* Mobile top bar */}
        <div className="md:hidden flex items-center justify-between bg-gray-900 text-white p-4">
          <div className="text-xl font-bold text-cyan-500">PMT</div>
          <button
            onClick={() => setSidebarOpen(true)}
            className="text-2xl font-bold"
          >
            ☰
          </button>
        </div>

        <main className="flex-1 p-8">{children}</main>
      </div>
    </div>
  );
}
